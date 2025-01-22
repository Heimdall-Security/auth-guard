package com.heimdallauth.server.commons.models.bifrost;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SuppressionListEmailEntry {
    private String emailAddress;
    private boolean isEmailDeliveryBlocked;
}
