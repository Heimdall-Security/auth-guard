package com.heimdallauth.server.commons.models.hydra;

import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JWKPrivateModel {
    private String id;
    private String encryptedJWK;
    private int cipherKeyVersion;
    private String encryptedJWKThumbprint;
    private String keyType;
    private int keySize;
    private String curveName;
    private Instant creationTimestamp;
    private Instant expirationTimestamp;
}
