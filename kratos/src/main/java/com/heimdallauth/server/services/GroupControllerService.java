package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.models.GroupModel;
import com.heimdallauth.server.datamanagers.GroupDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GroupControllerService {
    private final GroupDataManager groupDM;

    public GroupControllerService(GroupDataManager groupDM) {
        this.groupDM = groupDM;
    }
    public GroupModel createNewGroup(GroupModel groupModel){
        return groupDM.createGroup(groupModel);
    }
}
