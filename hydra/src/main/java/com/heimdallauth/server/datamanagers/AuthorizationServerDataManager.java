package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;

import java.util.List;

public interface AuthorizationServerDataManager {
    AuthorizationServerModel createAuthorizationServer(String serverName, String serverDescription, boolean isActive, List<String> authorizedServerIds);
    AuthorizationServerModel getAuthorizationServerById(String serverId);
    List<AuthorizationServerModel> getAuthorizationServers();
    List<AuthorizationServerModel> getActiveAuthorizationServers();
    List<AuthorizationServerModel> getInactiveAuthorizationServers();
    AuthorizationServerModel updateAuthorizationServer(String serverId, String serverName, String serverDescription, boolean isActive, List<String> authorizedServerIds);
    void deleteAuthorizationServer(String serverId);
}
