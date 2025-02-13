package com.heimdallauth.server.documents;

import com.heimdallauth.server.services.KeyManagementService;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JWKDocument {
    @Id
    private String id;
    private String encryptedJWK;
    private String encryptedJWKThumbprint;
    private KeyManagementService.KeyType keyType;
    private Instant creationTimestamp;
    private Instant expirationTimestamp;
}
