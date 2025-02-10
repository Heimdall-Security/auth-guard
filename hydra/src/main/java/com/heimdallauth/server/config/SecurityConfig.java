package com.heimdallauth.server.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//            authorizationManagerRequestMatcherRegistry.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
//            authorizationManagerRequestMatcherRegistry.requestMatchers("/api/hydra/swagger-ui.html", "/api/hydra/v3/api-docs", "/api/hydra/actuator/**").permitAll();
//            authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
//        });
//        return httpSecurity.build();
//    }
}
