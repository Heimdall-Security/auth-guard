package com.heimdallauth.server.commons.dto.bifrost;

import com.heimdallauth.server.commons.models.bifrost.TrackingOptions;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateConfigurationSetDTO {
    private String configurationSetName;
    private List<String> activeEmailSuppressionListsId;
    private TrackingOptions trackingOptions;
}
