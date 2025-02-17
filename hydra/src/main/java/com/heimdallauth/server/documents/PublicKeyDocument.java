package com.heimdallauth.server.documents;

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
public class PublicKeyDocument {
    @Id
    private String keyId;
    private String keyType;
    private String keyUse;
    private String keyAlgorithm;
    private Instant createdAt;
    private String linkedPrivateKeyId;
    private String publicKeyJson;
}
