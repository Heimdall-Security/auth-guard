package com.heimdallauth.server;

import com.heimdallauth.server.config.HeimdallHydraConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({HeimdallHydraConfiguration.class})
public class HydraApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydraApplication.class, args);
    }

}
