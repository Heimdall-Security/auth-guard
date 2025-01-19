package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.models.RoleModel;
import com.heimdallauth.server.datamanagers.RoleDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
