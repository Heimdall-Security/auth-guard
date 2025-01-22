package com.heimdallauth.server.commons.models.bifrost;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfigurationSet {
    private String configurationSetName;
    private List<EmailSuppressionList> activeEmailSuppressionLists;
    private TrackingOptions trackingOptions;
    private Instant creationTimestamp;
    private Instant lastUpdateTimestamp;
}
