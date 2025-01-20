package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.GroupModel;

import java.util.List;
import java.util.Optional;

public interface GroupDataManager {
    GroupModel createGroup(GroupModel groupModel);

    List<GroupModel> createGroup(List<GroupModel> groupModels);

    GroupModel updateGroup(String groupId, GroupModel updatedGroupModel);

    Optional<GroupModel> getGroup(String groupId);

    List<GroupModel> getGroups(List<String> groupIds);

    List<GroupModel> getGroupsByTenantId(String tenantId);

    List<GroupModel> getGroupsByTenantIdAndGroupName(String tenantId, String groupName);

    void addUserToGroupMembership(String groupId, String memberId);

    void addUsersToGroupMembership(String groupId, List<String> memberIds);

    void removeUserFromGroupMembership(String groupId, String memberId);

    void removeUsersFromGroupMembership(String groupId, List<String> memberIds);

    List<String> getGroupMembers(String groupId);

    Optional<GroupModel> searchGroup(String searchTerm);

    List<GroupModel> getAllGroups();

    GroupModel updateGroupRoleMapping(String groupId, List<String> roleIds);
}
