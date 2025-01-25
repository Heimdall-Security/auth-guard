package com.heimdallauth.server.commons.models.bifrost;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailSuppressionList {
    private String id;
    private String suppressionListName;
    private Instant creationTimestamp;
    private Instant lastUpdateTimestamp;
    private List<SuppressionListEmailEntry> emailSuppressions;
}
