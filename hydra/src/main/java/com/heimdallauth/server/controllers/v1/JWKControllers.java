package com.heimdallauth.server.controllers.v1;

import com.heimdallauth.server.services.KeyManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/.jwks")
public class JWKControllers {
    private final KeyManagementService kms;

    public JWKControllers(KeyManagementService kms) {
        this.kms = kms;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> exposeJWK() {
        return ResponseEntity.ok(this.kms.getPublicJWKStore());
    }
}
