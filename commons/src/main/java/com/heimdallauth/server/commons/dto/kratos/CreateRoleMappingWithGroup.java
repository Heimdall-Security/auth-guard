package com.heimdallauth.server.commons.dto.kratos;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateRoleMappingWithGroup {
    private String groupId;
    private List<String> rolesId;
}
