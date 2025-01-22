package com.heimdallauth.server.documents;

import com.heimdallauth.server.constants.MongoCollectionConstants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = MongoCollectionConstants.CONFIGURATION_SET_COLLECTION)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfigurationSetDocument {
    @Id
    private String id;
    @Indexed
    private String configurationSetName;
    private List<String> activeEmailSuppressionLists;
    private String trackingOptionsId;
    private Instant creationTimestamp;
    private Instant lastUpdateTimestamp;
}
