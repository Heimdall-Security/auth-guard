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
    private String protocol;

    public String getIssuerUrl(String serverId){
        return String.format("%s://%s.%s/%s", this.protocol, this.deploymentName, this.domainName, serverId);
    }
}
