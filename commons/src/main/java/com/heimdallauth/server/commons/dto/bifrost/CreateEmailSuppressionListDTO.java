package com.heimdallauth.server.commons.dto.bifrost;

import com.heimdallauth.server.commons.models.bifrost.SuppressionListEmailEntry;
import lombok.*;

import java.util.List;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateEmailSuppressionListDTO {
    private String suppressionListName;
    private List<String> emailAddresses;
    private boolean isEmailBlocked;
}
