package com.heimdallauth.server.commons.models.kratos;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupModel {
    private String id;
    private String hrn;
    private String tenantId;
    private String groupName;
    private String groupDescription;
    private List<RoleModel> roles;
    private Instant createdOn;
    private Instant lastUpdatedOn;
}
