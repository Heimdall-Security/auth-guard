package com.heimdallauth.server.datamanagers;

import com.nimbusds.jose.jwk.JWK;

import java.security.KeyPair;
import java.util.List;

public interface KeyDataManager {
    String storeCryptographyKey(KeyPair keyPair);
    JWK getPrivateKey(String keyId);
    List<JWK> getAllPublicKeys();
    String rotateJWK(KeyPair updatedKeyPair);
}
