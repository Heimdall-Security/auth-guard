package com.heimdallauth.server.documents;

import com.heimdallauth.server.constants.MongoCollectionConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = MongoCollectionConstants.EMAIL_SUPPRESSION_LIST_COLLECTION)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailSuppressionListDocument {
    @Id
    private String id;
    private List<String> emailSuppressionEntryIds;
    private Instant creationTimestamp;
    private Instant lastUpdateTimestamp;
}
