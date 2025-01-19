package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.models.RoleModel;
import com.heimdallauth.server.services.RoleControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/roles")
public class RoleController {
    private final RoleControllerService roleControllerService;

    public RoleController(RoleControllerService roleControllerService) {
        this.roleControllerService = roleControllerService;
    }

    @PostMapping("/role")
    public ResponseEntity<RoleModel> createNewRole(@RequestBody RoleModel createRolePayload){
        return ResponseEntity.ok(this.roleControllerService.createNewRole(createRolePayload));
    }
}
