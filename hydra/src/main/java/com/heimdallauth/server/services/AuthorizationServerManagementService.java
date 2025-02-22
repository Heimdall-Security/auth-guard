package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.dto.hydra.CreateAuthorizationServerDTO;
import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;
import com.heimdallauth.server.datamanagers.AuthorizationServerDataManager;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthorizationServerManagementService {
    private final AuthorizationServerDataManager authServerDM;
    private final KeyManagementService kms;

    public AuthorizationServerManagementService(AuthorizationServerDataManager authServerDM, KeyManagementService kms) {
        this.authServerDM = authServerDM;
        this.kms = kms;
    }

    public AuthorizationServerModel createAuthorizationServer(CreateAuthorizationServerDTO authorizationServerCreatePayload) {
        String signingKeyId = kms.generateSigningKeyForAuthorizationServer(KeyManagementService.KeyAlgorithm.EC, Optional.empty());
        return authServerDM.createAuthorizationServer(
                authorizationServerCreatePayload.getAuthorizationServerName(),
                authorizationServerCreatePayload.getAuthorizationServerDescription(),
                authorizationServerCreatePayload.isActive(),
                authorizationServerCreatePayload.getAuthorizedServerIds(),
                signingKeyId
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
        authServerDM.updateAuthorizationServer(
                serverId,
                authorizationServerUpdatePayload.getAuthorizationServerName(),
                authorizationServerUpdatePayload.getAuthorizationServerDescription(),
                authorizationServerUpdatePayload.isActive(),
                Collections.emptyList() //TODO remove and properly implement
        );
        return authServerDM.getAuthorizationServerById(serverId);   //Another step for caching changes
    }
}
