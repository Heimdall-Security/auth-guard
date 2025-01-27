package com.heimdallauth.server.documents;

import com.heimdallauth.server.commons.models.bifrost.SuppressionListEmailEntry;
import com.heimdallauth.server.constants.MongoCollectionConstants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = MongoCollectionConstants.EMAIL_SUPPRESSION_LIST_COLLECTION)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSuppressionListDocument {
    @Id
    private String id;
    private String suppressionListName;
    private List<SuppressionListEmailEntry> emailSuppressions;
    private Instant creationTimestamp;
    private Instant lastUpdateTimestamp;
}
