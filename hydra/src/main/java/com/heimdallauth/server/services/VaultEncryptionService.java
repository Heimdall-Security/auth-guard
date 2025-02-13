package com.heimdallauth.server.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultTransitOperations;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class VaultEncryptionService {
    private static final String TRANSIT_ENGINE_NAME = "heimdall-encryption";
    private static final String KEY_NAME = "hydra-jwt-encryption-key";
    private final VaultTransitOperations vaultTransitOperations;
    protected int latestKeyVersion = 0;

    public VaultEncryptionService(VaultTemplate vaultTemplate) {
        this.vaultTransitOperations = vaultTemplate.opsForTransit(TRANSIT_ENGINE_NAME);
    }
    @Scheduled(fixedRate = 100*60*60*2)
    @PostConstruct
    private void getLatestKey(){
        this.latestKeyVersion = Objects.requireNonNull(this.vaultTransitOperations.getKey(KEY_NAME)).getLatestVersion();
    }

    protected String encrypt(String value){
        return vaultTransitOperations.encrypt(KEY_NAME, value);
    }
    protected String decrypt(String encryptedValue){
        return vaultTransitOperations.decrypt(KEY_NAME, encryptedValue);
    }
    private String reWrapEncryptedData(String encryptedValue){
        return vaultTransitOperations.rewrap(KEY_NAME, encryptedValue);
    }
}
