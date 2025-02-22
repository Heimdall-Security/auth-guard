package com.heimdallauth.server.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class PrivateKeyDocument {
    @Id
    private String id;
    private String encryptedPrivateKey;
    private String keyThumbprint;
    private String keyUse;
    private String keyAlgorithm;
}