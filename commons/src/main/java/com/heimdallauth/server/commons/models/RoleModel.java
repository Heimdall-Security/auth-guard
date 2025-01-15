package com.heimdallauth.server.commons.models;

import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleModel {
    private String id;
    private String hrn;
    private String roleName;
    private String roleDescription;
    private Instant createdOn;
    private Instant lastUpdatedOn;
}
