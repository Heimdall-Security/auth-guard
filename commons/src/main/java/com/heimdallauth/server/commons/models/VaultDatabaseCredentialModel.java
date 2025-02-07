package com.heimdallauth.server.commons.models;

import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VaultDatabaseCredentialModel {
    private String username;
    private String password;
    private String leaseId;
    private Instant leaseExpiryTimestamp;
}
