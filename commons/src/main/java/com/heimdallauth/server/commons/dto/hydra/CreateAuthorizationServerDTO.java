package com.heimdallauth.server.commons.dto.hydra;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateAuthorizationServerDTO {
    private String authorizationServerName;
    private String authorizationServerDescription;
    private boolean isActive;
    private List<String> authorizedServerIds;
}
