package com.heimdallauth.server.documents;

import com.heimdallauth.server.constants.MongoCollectionConstants;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = MongoCollectionConstants.TRACKING_OPTIONS_COLLECTION)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrackingOptionsDocument {
    private String id;
    private boolean useCustomTrackingDomain;
    private String customTrackingDomain;
    private String customTrackingSubdomain;
    private boolean useCustomClickTrackingDomain;
}
