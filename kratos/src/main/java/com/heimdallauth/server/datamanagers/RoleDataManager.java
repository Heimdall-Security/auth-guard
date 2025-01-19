package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.RoleModel;

import java.util.List;
import java.util.Optional;

public interface RoleDataManager {
    RoleModel createRole(String roleName, String roleDescription);
    Optional<RoleModel> getRoleById(String roleId);
    List<RoleModel> getRolesByIds(List<String> ids);
    Optional<RoleModel> searchRoleUsingRoleNameOrRoleDescription(String searchTerm);
    Optional<RoleModel> updateRole(String roleId, String roleName, String roleDescription);
    List<RoleModel> getAllRoles();
}
