package com.heimdallauth.server.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "heimdall.hydra")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HeimdallHydraConfiguration {
    private String domainName;
    private String deploymentName;

}
