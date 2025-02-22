package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.dto.hydra.CreateAuthorizationServerDTO;
import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;
import com.heimdallauth.server.datamanagers.AuthorizationServerDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AuthorizationServerManagementService {
    private final AuthorizationServerDataManager authServerDM;

    public AuthorizationServerManagementService(AuthorizationServerDataManager authServerDM) {
        this.authServerDM = authServerDM;
    }

    public AuthorizationServerModel createAuthorizationServer(CreateAuthorizationServerDTO authorizationServerCreatePayload) {
        return authServerDM.createAuthorizationServer(
                authorizationServerCreatePayload.getAuthorizationServerName(),
                authorizationServerCreatePayload.getAuthorizationServerDescription(),
                authorizationServerCreatePayload.isActive(),
                authorizationServerCreatePayload.getAuthorizedServerIds()
        );
    }
    public List<AuthorizationServerModel> getAuthorizationServers() {
        return authServerDM.getAuthorizationServers();
    }
    public List<AuthorizationServerModel> getActiveAuthorizationServers() {
        return authServerDM.getActiveAuthorizationServers();
    }
    public List<AuthorizationServerModel> getInactiveAuthorizationServers() {
        return authServerDM.getInactiveAuthorizationServers();
    }
    public AuthorizationServerModel getAuthorizationServerById(String serverId) {
        return authServerDM.getAuthorizationServerById(serverId);
    }
    public AuthorizationServerModel updateAuthorizationServer(String serverId, AuthorizationServerModel authorizationServerUpdatePayload) {
        return authServerDM.updateAuthorizationServer(
                serverId,
                authorizationServerUpdatePayload.getAuthorizationServerName(),
                authorizationServerUpdatePayload.getAuthorizationServerDescription(),
                authorizationServerUpdatePayload.isActive(),
                Collections.emptyList() //TODO remove and properly implement
        );
    }
}
