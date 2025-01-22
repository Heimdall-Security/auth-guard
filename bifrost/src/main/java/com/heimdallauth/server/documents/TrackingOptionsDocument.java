package com.heimdallauth.server.documents;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
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
