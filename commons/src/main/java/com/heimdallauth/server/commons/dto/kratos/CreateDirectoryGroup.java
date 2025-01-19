package com.heimdallauth.server.commons.dto.kratos;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateDirectoryGroup {
    private String groupName;
    private String groupDescription;
}
