package com.heimdallauth.server.models;

import com.heimdallauth.server.dao.GroupMembershipDocument;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RolesToGroupAggregationModel {
    private String id;
    private String roleName;
    private String roleDescription;
    private List<GroupMembershipDocument> groupRoleMembership;
    private Instant createdOn;
    private Instant lastUpdateOn;

}
