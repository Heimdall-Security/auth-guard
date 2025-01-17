package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.RoleModel;

import java.util.Optional;

public interface RoleDataManager {
    RoleModel createRole(String roleName, String roleDescription);
    Optional<RoleModel> getRole(String roleId);
    Optional<RoleModel> searchRoleUsingRoleNameOrRoleDescription(String searchTerm);
    Optional<RoleModel> updateRole(String roleId, String roleName, String roleDescription);
}
