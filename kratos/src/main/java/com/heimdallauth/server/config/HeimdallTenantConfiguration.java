package com.heimdallauth.server.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "heimdall.tenant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HeimdallTenantConfiguration {
    private String tenantId;
    private String tenantLocationCountryCode;
    private String dataCenterId;
    private String dataCenterCountryCode;
    private String dataCenterLocation;
}
