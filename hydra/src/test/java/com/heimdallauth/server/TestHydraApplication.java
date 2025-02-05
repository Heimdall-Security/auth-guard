package com.heimdallauth.server;

import org.springframework.boot.SpringApplication;

public class TestHydraApplication {

    public static void main(String[] args) {
        SpringApplication.from(HydraApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
