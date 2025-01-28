package com.heimdallauth.server.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "heimdall.smtp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeimdallSmtpConfiguration {
    private String host;
    private String port;
    private String loginUsername;
    private String loginPassword;
    private String fromEmail;
}
