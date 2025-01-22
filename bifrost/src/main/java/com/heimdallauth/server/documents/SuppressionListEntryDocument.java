package com.heimdallauth.server.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuppressionListEntryDocument {
    @Id
    private String id;
    @Indexed
    private String emailAddress;
    private boolean isEmailDeliveryBlocked;
}
