package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.models.RoleModel;
import com.heimdallauth.server.datamanagers.RoleDataManager;
import com.heimdallauth.server.exceptions.RoleNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleControllerService {
    private final RoleDataManager roleDM;

    public RoleControllerService(RoleDataManager roleDM) {
        this.roleDM = roleDM;
    }

    public RoleModel createNewRole(RoleModel createRolePayload) {
        return roleDM.createRole(
                createRolePayload.getRoleName(),
                createRolePayload.getRoleDescription()
        );
    }

    public List<RoleModel> getAllRoles() {
        return roleDM.getAllRoles();
    }

    public RoleModel getRoleById(String roleId) {
        return roleDM.getRoleById(roleId).orElseThrow(() -> new RoleNotFound("Role not found", roleId));
    }
}
