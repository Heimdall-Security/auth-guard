package com.heimdallauth.server.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginAuditEntryDocument {
    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String userProfileId;
    private Instant loginTimestamp;
    private String ipAddress;
    private String complianceEvaluationResult;
    private String userAgent;
}
