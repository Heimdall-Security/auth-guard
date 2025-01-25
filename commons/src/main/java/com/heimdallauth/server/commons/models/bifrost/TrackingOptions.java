package com.heimdallauth.server.commons.models.bifrost;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrackingOptions {
    private boolean useCustomTrackingDomain;
    private String customTrackingDomain;
    private String customTrackingSubdomain;
    private boolean useCustomClickTrackingDomain;
}
