package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.models.hydra.JWKPrivateModel;
import com.heimdallauth.server.datamanagers.KMSDataManager;
import com.heimdallauth.server.utils.CryptoUtils;
import com.heimdallauth.server.utils.RandomIdGeneratorUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@EnableScheduling
public class KeyManagementService {
    private final KMSDataManager dataManager;
    private final VaultEncryptionService vaultEncryptionService;
    private static final int KEY_SIZE = 2048;

    private  List<Map<String, Object>> publicJWKs = new ArrayList<>();


    public KeyManagementService(KMSDataManager dataManager, VaultEncryptionService vaultEncryptionService) {
        this.dataManager = dataManager;
        this.vaultEncryptionService = vaultEncryptionService;
        this.publicJWKs = loadPublicJWK();
    }

    public enum KeyType {
        RSA,
        EC
    }

    public static KeyPair keyPairGeneratorHelper(KeyType type){
        return switch (type) {
            case RSA -> CryptoUtils.generateRSAKeyPair(KEY_SIZE);
            case EC -> CryptoUtils.generateECKeyPair();
            default -> null;
        };
    }
    public String generateSigningKeyForToken(KeyType keyType) throws JOSEException {
        String keyDatabaseId = RandomIdGeneratorUtil.generateRandomServerId();
        JWK generatedSecretKey = CryptoUtils.convertToJWKPrivate(keyPairGeneratorHelper(keyType));
        this.dataManager.storeJWK(
                keyDatabaseId,
                vaultEncryptionService.encrypt(generatedSecretKey.toJSONString()),
                generatedSecretKey.computeThumbprint().toString(),
                keyType.toString()
        );
        this.publicJWKs.add(generatedSecretKey.toPublicJWK().toJSONObject());
        return keyDatabaseId;
    }
    public String getKeyFromDB(String keyId) throws ParseException {
        JWKPrivateModel privateKeyModel = this.dataManager.getJWK(keyId);
        JWK secretKey = JWK.parse(vaultEncryptionService.decrypt(privateKeyModel.getEncryptedJWK()));
        secretKey.toPublicJWK();
        return "null";
    }
    public List<Map<String, Object>> getAllKeysPublic(){
        return this.publicJWKs;
    }

    @Scheduled(fixedRate = 100*60*60)
    private void hourlyTasks(){
        this.publicJWKs = loadPublicJWK();
    }
    private List<Map<String, Object>> loadPublicJWK(){
        List<JWKPrivateModel>  jwk = this.dataManager.getAllJWKs();
        List<JWK> secretJwks = jwk.stream().map(JWKPrivateModel::getEncryptedJWK).map(j -> {
            try{
                return JWK.parse(vaultEncryptionService.decrypt(j)).toPublicJWK();
            } catch (ParseException e){
                log.error("Error parsing JWK: {}", e.getMessage());
                return null;
            }
        }).toList();
        return secretJwks.stream().map(JWK::toPublicJWK).map(JWK::toJSONObject).toList();
    }
}
