package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.dto.kratos.CreateDirectoryGroup;
import com.heimdallauth.server.commons.models.GroupModel;
import com.heimdallauth.server.commons.models.RoleModel;
import com.heimdallauth.server.datamanagers.GroupDataManager;
import com.heimdallauth.server.datamanagers.RoleDataManager;
import com.heimdallauth.server.exceptions.RoleNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GroupControllerService {
    private final GroupDataManager groupDM;
    private final RoleDataManager roleDM;

    public GroupControllerService(GroupDataManager groupDM, RoleDataManager roleDM) {
        this.groupDM = groupDM;
        this.roleDM = roleDM;
    }
    public GroupModel createNewGroup(CreateDirectoryGroup createDirectoryGroup) {
        return groupDM.createGroup(
                GroupModel.builder()
                        .groupName(createDirectoryGroup.getGroupName())
                        .groupDescription(createDirectoryGroup.getGroupDescription())
                        .build()
        );
    }
    public List<GroupModel> getAllGroups() {
        return groupDM.getAllGroups();
    }

    public GroupModel updateGroupRoleMapping(String groupId, List<String> rolesIds){
        List<String> invalidRoleIds = new ArrayList<>();
        try {
            List<RoleModel> validatedRoles = rolesIds.stream().map(roleId -> this.roleDM.getRoleById(roleId).orElseThrow(() -> new RoleNotFound(String.format("The role with requestedId %s could not be found", roleId)))).toList();
            return groupDM.updateGroupRoleMapping(groupId, validatedRoles.stream().map(RoleModel::getId).toList());
        }catch (RoleNotFound roleNotFound){
            invalidRoleIds.add(roleNotFound.getMessage());
        }
        throw new RoleNotFound(String.format("The following roleIds are invalid: %s", invalidRoleIds));
    }
}
