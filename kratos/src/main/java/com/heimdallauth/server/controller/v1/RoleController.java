package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.models.kratos.RoleModel;
import com.heimdallauth.server.services.RoleControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/roles")
public class RoleController {
    private final RoleControllerService roleControllerService;

    public RoleController(RoleControllerService roleControllerService) {
        this.roleControllerService = roleControllerService;
    }

    @PostMapping("/role")
    public ResponseEntity<RoleModel> createNewRole(@RequestBody RoleModel createRolePayload) {
        return ResponseEntity.ok(this.roleControllerService.createNewRole(createRolePayload));
    }

    @GetMapping
    public ResponseEntity<List<RoleModel>> getAllRoles() {
        return ResponseEntity.ok(this.roleControllerService.getAllRoles());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleModel> getRoleById(@PathVariable("roleId") String roleId) {
        return ResponseEntity.ok(this.roleControllerService.getRoleById(roleId));
    }
}
