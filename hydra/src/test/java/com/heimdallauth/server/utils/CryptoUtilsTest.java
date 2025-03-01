package com.heimdallauth.server.utils;

import com.nimbusds.jose.jwk.JWK;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void generateRSAKeyPair() {
        KeyPair kp = CryptoUtils.generateRSAKeyPair(2048);
        assertNotNull(kp);
        assertEquals("RSA", kp.getPrivate().getAlgorithm());
    }

    @Test
    void generateECKeyPair() {
    }

    @Test
    void convertRSAKeyPairToJWK() {
        KeyPair kp = CryptoUtils.generateRSAKeyPair(2048);
        assertNotNull(kp);
        assertEquals("RSA", kp.getPrivate().getAlgorithm());
        JWK jwk = CryptoUtils.convertToJWKPublic(kp);
        assertNotNull(jwk);
    }
    @Test
    void convertRSAKeyPairToJWK4096() {
        KeyPair kp = CryptoUtils.generateRSAKeyPair(4096);
        assertNotNull(kp);
        assertEquals("RSA", kp.getPrivate().getAlgorithm());
        JWK jwk = CryptoUtils.convertToJWKPublic(kp);
        assertNotNull(jwk);
    }
}