package com.heimdallauth.server.controllers.v1;

import com.heimdallauth.server.commons.dto.hydra.CreateAuthorizationServerDTO;
import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;
import com.heimdallauth.server.services.AuthorizationServerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/manage/authorization-servers")
public class AuthorizationServerManagementController {
    private final AuthorizationServerManagementService authorizationServerManagementService;

    @Autowired
    public AuthorizationServerManagementController(AuthorizationServerManagementService authorizationServerManagementService) {
        this.authorizationServerManagementService = authorizationServerManagementService;
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<AuthorizationServerModel> getAuthorizationServerById(@PathVariable("serverId") String serverId) {
        return ResponseEntity.ok(authorizationServerManagementService.getAuthorizationServerById(serverId));
    }
    @PostMapping
    public ResponseEntity<AuthorizationServerModel> createAuthorizationServer(@RequestBody CreateAuthorizationServerDTO createAuthorizationServerDTO) {
        return ResponseEntity.ok(authorizationServerManagementService.createAuthorizationServer(createAuthorizationServerDTO));
    }
    @PutMapping("/{serverId}")
    public ResponseEntity<AuthorizationServerModel> updateAuthorizationServer(@PathVariable("serverId") String serverId, @RequestBody AuthorizationServerModel authorizationServerModel) {
        return ResponseEntity.ok(authorizationServerManagementService.updateAuthorizationServer(serverId, authorizationServerModel));
    }
}
