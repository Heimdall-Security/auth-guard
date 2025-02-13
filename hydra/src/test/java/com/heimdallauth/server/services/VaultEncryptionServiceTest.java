package com.heimdallauth.server.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("local")
class VaultEncryptionServiceTest {

    @Autowired
    private VaultEncryptionService vaultEncryptionService;

    @Test
    void encrypt() {
        String secretText = "secret";
        String encryptedText = vaultEncryptionService.encrypt(secretText);
        assertNotNull(encryptedText);
    }

    @Test
    void decrypt() {
        String secretString = "SuperSecretStringForDecryption";
        String encryptedString = vaultEncryptionService.encrypt(secretString);
        String decryptedString = vaultEncryptionService.decrypt(encryptedString);
        assertEquals(secretString, decryptedString);
    }
}