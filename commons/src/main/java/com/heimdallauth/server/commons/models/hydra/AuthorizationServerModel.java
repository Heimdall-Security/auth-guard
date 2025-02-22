package com.heimdallauth.server.commons.models.hydra;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorizationServerModel {
    private String id;
    private String authorizationServerName;
    private String authorizationServerDescription;
    private String issueUrl;
    private String signingKeyId;
    private boolean isActive;
    private List<ClientModel> clients;
    private List<AuthorizationServerModel> authorizedServers;
}
