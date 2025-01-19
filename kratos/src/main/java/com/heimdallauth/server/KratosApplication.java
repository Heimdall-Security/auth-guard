package com.heimdallauth.server;

import com.heimdallauth.server.config.HeimdallTenantConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({HeimdallTenantConfiguration.class})
public class KratosApplication {

    public static void main(String[] args) {
        SpringApplication.run(KratosApplication.class, args);
    }

}
