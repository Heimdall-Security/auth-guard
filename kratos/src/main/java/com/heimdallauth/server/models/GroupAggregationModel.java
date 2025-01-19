package com.heimdallauth.server.models;

import com.heimdallauth.server.commons.models.GroupModel;
import com.heimdallauth.server.commons.models.RoleModel;
import com.heimdallauth.server.dao.GroupMembershipDocument;
import com.heimdallauth.server.dao.RoleDocument;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupAggregationModel {
    private String id;
    private String groupName;
    private String groupDescription;
    private Instant createdOn;
    private Instant lastUpdatedOn;
    private List<GroupMembershipDocument> rolesId;
    private List<RoleDocument> roles;

    public GroupModel toGroupModel(){
        return GroupModel.builder()
                .id(this.id)
                .groupName(this.groupName)
                .groupDescription(this.groupDescription)
                .roles(this.roles.stream().map(s -> RoleModel.builder()
                        .id(s.getId())
                        .roleName(s.getRoleName())
                        .roleDescription(s.getRoleDescription())
                        .lastUpdatedOn(s.getLastUpdatedOn())
                        .createdOn(s.getCreatedOn())
                        .build()).toList())
                .createdOn(this.createdOn)
                .lastUpdatedOn(this.lastUpdatedOn)
                .build();
    }
}
