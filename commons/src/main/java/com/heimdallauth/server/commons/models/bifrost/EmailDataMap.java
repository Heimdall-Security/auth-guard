package com.heimdallauth.server.commons.models.bifrost;

import com.heimdallauth.server.commons.models.BrandModel;
import com.heimdallauth.server.commons.models.kratos.UserProfileModel;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailDataMap {
    private UserProfileModel userInformation;
    private BrandModel brandingSettings;
}
