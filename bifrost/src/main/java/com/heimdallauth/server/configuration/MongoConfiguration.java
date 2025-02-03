package com.heimdallauth.server.configuration;

import com.heimdallauth.server.commons.models.VaultDatabaseCredentialModel;
import com.heimdallauth.server.exceptions.DBNotReady;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.time.Instant;

@Configuration
@Profile({"mongo","vault"})
@Slf4j
public class MongoConfiguration {
    private static final String DATABASE_ENGINE = "heimdall-db-credentials";
    private static final String DATABASE_ROLE = "bifrost-db-read-write-role";
    private static final String DATABASE_NAME = "bifrost-db";
    @Value("${MONGO_HOST}")
    private String mongoHost;

    @Bean
    public VaultDatabaseCredentialModel dbCredentials(VaultTemplate vaultTemplate) {
        return getCredentialsFromVault(vaultTemplate);
    }
    @Bean
    @Retry(name = "mongoInitRetry", fallbackMethod = "mongoClientFallbackMethod")
    public MongoClient mongoClient(VaultDatabaseCredentialModel dbCredentials) throws InterruptedException {
        log.info("Vault lease id : {}, lease expiry timestamp : {}", dbCredentials.getLeaseId(), dbCredentials.getLeaseExpiryTimestamp());
        log.info("Waiting for mongo-atlas to be ready");
        Thread.sleep(15*1000); // Wait 15 seconds to allow mongo to be ready
        String connectionString = String.format(mongoHost, dbCredentials.getUsername(), dbCredentials.getPassword());
        return MongoClients.create(connectionString);
    }
    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        log.info("Creating mongo template");
        try{
            return new MongoTemplate(mongoClient, DATABASE_NAME);
        }catch (Exception e){
            throw new DBNotReady("Failed to create mongo template");
        }
    }

    private VaultDatabaseCredentialModel getCredentialsFromVault(VaultTemplate vaultTemplate) {
        VaultResponse vaultResponse = vaultTemplate.read(String.format("%s/creds/%s", DATABASE_ENGINE, DATABASE_ROLE));
        return VaultDatabaseCredentialModel.builder()
                .username((String) vaultResponse.getData().get("username"))
                .password((String)vaultResponse.getData().get("password"))
                .leaseId(vaultResponse.getLeaseId())
                .leaseExpiryTimestamp(Instant.ofEpochMilli(vaultResponse.getLeaseDuration()))
                .build();
    }
    public MongoClient mongoClientFallbackMethod(VaultTemplate vaultTemplate) throws Exception {
        throw new Exception("Failed to get credentials from vault");
    }

}
