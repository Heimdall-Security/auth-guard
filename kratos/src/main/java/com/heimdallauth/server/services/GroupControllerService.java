package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.dto.kratos.CreateDirectoryGroup;
import com.heimdallauth.server.commons.models.GroupModel;
import com.heimdallauth.server.datamanagers.GroupDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GroupControllerService {
    private final GroupDataManager groupDM;

    public GroupControllerService(GroupDataManager groupDM) {
        this.groupDM = groupDM;
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
}
