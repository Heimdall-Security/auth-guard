package com.heimdallauth.server.services;

import com.heimdallauth.server.datamanagers.KeyDataManager;
import com.heimdallauth.server.utils.CryptoUtils;
import com.nimbusds.jose.jwk.JWK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@Slf4j
@EnableScheduling
public class KeyManagementService {
    private final KeyDataManager kmsDM;

    public KeyManagementService(KeyDataManager kmsDM) {
        this.kmsDM = kmsDM;
    }

    public enum KeyAlgorithm {
        EC,
        RSA
    }

    public String generateSigningKeyForAuthorizationServer(KeyAlgorithm keyAlgorithm, Optional<Integer> rsaKeySize) {
        assertNotNull(keyAlgorithm, "Key Algorithm cannot be null");
        KeyPair generatedKeyPair = switch (keyAlgorithm) {
            case EC -> CryptoUtils.generateECKeyPair();
            case RSA -> CryptoUtils.generateRSAKeyPair(rsaKeySize.orElse(2048));
        };
        return kmsDM.storeCryptographyKey(generatedKeyPair);
    }
    public List<Map<String, Object>> getPublicJWKStore(){
        return kmsDM.getAllPublicKeys().stream().map(JWK::toJSONObject).toList();
    }
}
