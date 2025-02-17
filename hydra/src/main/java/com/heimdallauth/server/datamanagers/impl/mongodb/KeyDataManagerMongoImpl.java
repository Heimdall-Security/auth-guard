package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.datamanagers.KeyDataManager;
import com.heimdallauth.server.documents.PrivateKeyDocument;
import com.heimdallauth.server.documents.PublicKeyDocument;
import com.heimdallauth.server.services.VaultEncryptionService;
import com.heimdallauth.server.utils.CryptoUtils;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class KeyDataManagerMongoImpl implements KeyDataManager {
    private static final String PRIVATE_KEY_COLLECTION = "private-key-collection";
    private static final String PUBLIC_KEY_COLLECTION = "public-key-collection";
    private final VaultEncryptionService vaultEncryptionService;
    private final MongoTemplate mongoTemplate;


    public KeyDataManagerMongoImpl(VaultEncryptionService vaultEncryptionService, MongoTemplate mongoTemplate) {
        this.vaultEncryptionService = vaultEncryptionService;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    @Transactional
    public String storeCryptographyKey(KeyPair keyPair) {
        JWK jwkPrivateKeyComponent = CryptoUtils.convertToJWKPrivate(keyPair);
        PublicKeyDocument publicKeyDocument = convertJWKtoPublicKeyDocument(jwkPrivateKeyComponent);
        PrivateKeyDocument privateKeyDocument = convertJWWKToPrivateKeyDocument(
                vaultEncryptionService.encrypt(jwkPrivateKeyComponent.toJSONString()),
                jwkPrivateKeyComponent.getKeyID(),
                jwkPrivateKeyComponent.getKeyUse().getValue(),
                jwkPrivateKeyComponent.getAlgorithm().getName());
        this.mongoTemplate.save(publicKeyDocument, PUBLIC_KEY_COLLECTION);
        this.mongoTemplate.save(privateKeyDocument, PRIVATE_KEY_COLLECTION);
        return jwkPrivateKeyComponent.getKeyID();
    }

    @Override
    public JWK getPrivateKey(String keyId) {
        Query query = Query.query(Criteria.where("id").is(keyId));
        Optional<PrivateKeyDocument> privateKeyDocument = Optional.ofNullable(this.mongoTemplate.findOne(query, PrivateKeyDocument.class, PRIVATE_KEY_COLLECTION));
        if (privateKeyDocument.isPresent()) {
            try {
                return JWK.parse(vaultEncryptionService.decrypt(privateKeyDocument.get().getEncryptedPrivateKey()));
            } catch (ParseException e) {
                log.error("Error parsing JWK from database", e);
            }
        }
        throw new RuntimeException("Key not found");
    }

    @Override
    public List<JWK> getAllPublicKeys() {
        return this.mongoTemplate.findAll(PublicKeyDocument.class, PUBLIC_KEY_COLLECTION).stream().map(publicKeyDocument -> {
            try {
                return JWK.parse(publicKeyDocument.getPublicKeyJson());
            } catch (ParseException e) {
                log.error("Error parsing JWK from database", e);
            }
            return null;
        }).toList();
    }

    @Override
    public String rotateJWK(KeyPair updatedKeyPair) {
        //TODO implement a method to rotate the key-pairs.
        return "";
    }

    private static PublicKeyDocument convertJWKtoPublicKeyDocument(JWK privateJWK) {
        JWK publicJWKComponent = privateJWK.toPublicJWK();
        return PublicKeyDocument.builder()
                .keyId(publicJWKComponent.getKeyID())
                .keyAlgorithm(publicJWKComponent.getAlgorithm().getName())
                .keyType(publicJWKComponent.getKeyType().getValue())
                .linkedPrivateKeyId(privateJWK.getKeyID())
                .publicKeyJson(publicJWKComponent.toJSONString())
                .build();
    }

    private static PrivateKeyDocument convertJWWKToPrivateKeyDocument(String encryptedJWKString, String keyId, String keyUse, String keyAlgorithm) {
        return PrivateKeyDocument.builder()
                .id(keyId)
                .encryptedPrivateKey(encryptedJWKString)
                .keyThumbprint(keyId)
                .keyUse(keyUse)
                .keyAlgorithm(keyAlgorithm)
                .build();
    }
}
