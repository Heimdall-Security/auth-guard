package com.heimdallauth.server.documents;

import com.heimdallauth.server.constants.MongoCollectionConstants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = MongoCollectionConstants.EMAIL_ENTRY_COLLECTION)
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
