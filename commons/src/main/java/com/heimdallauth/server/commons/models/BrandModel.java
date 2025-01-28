package com.heimdallauth.server.commons.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BrandModel {
    private String brandName;
    private String brandDescription;
    private String logoUrl;
    private String logoAltText;
    private String brandPrimaryColor;
    private String brandSecondaryColor;
    private String brandDarkModePrimaryColor;
    private String brandDarkModeSecondaryColor;
}
