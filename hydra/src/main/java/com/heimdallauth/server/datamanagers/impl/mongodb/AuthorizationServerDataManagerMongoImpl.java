package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;
import com.heimdallauth.server.config.HeimdallHydraConfiguration;
import com.heimdallauth.server.datamanagers.AuthorizationServerDataManager;
import com.heimdallauth.server.documents.AuthorizationServerDocument;
import com.heimdallauth.server.utils.RandomIdGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorizationServerDataManagerMongoImpl implements AuthorizationServerDataManager {
    private final MongoTemplate mongoTemplate;
    private final HeimdallHydraConfiguration heimdallHydraConfiguration;

    @Autowired
    public AuthorizationServerDataManagerMongoImpl(MongoTemplate mongoTemplate, HeimdallHydraConfiguration heimdallHydraConfiguration) {
        this.mongoTemplate = mongoTemplate;
        this.heimdallHydraConfiguration = heimdallHydraConfiguration;
    }

    @Override
    public AuthorizationServerModel createAuthorizationServer(String serverName, String serverDescription, boolean isActive, List<String> authorizedServerIds) {
        AuthorizationServerDocument authorizationServerDocument = AuthorizationServerDocument.builder()
                .id(RandomIdGeneratorUtil.generateRandomServerId())
                .authorizationServerName(serverName)
                .authorizationServerDescription(serverDescription)
                .issueUrl(heimdallHydraConfiguration.getDeploymentName() + heimdallHydraConfiguration.getDomainName() + "/oauth2/token")
                .isActive(isActive)
                .authorizedServerIds(authorizedServerIds)
                .build();
        return mongoTemplate.save(authorizationServerDocument).toAuthorizationServerModel();
    }

    @Override
    public AuthorizationServerModel getAuthorizationServerById(String serverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(serverId));
        AuthorizationServerDocument authorizationServerDocument = Optional.ofNullable(mongoTemplate.findOne(query, AuthorizationServerDocument.class)).orElseThrow(() -> new RuntimeException("Authorization Server not found"));
        return null;
    }

    @Override
    public List<AuthorizationServerModel> getAuthorizationServers() {
        return List.of();
    }

    @Override
    public List<AuthorizationServerModel> getActiveAuthorizationServers() {
        return List.of();
    }

    @Override
    public List<AuthorizationServerModel> getInactiveAuthorizationServers() {
        return List.of();
    }

    @Override
    public AuthorizationServerModel updateAuthorizationServer(String serverId, String serverName, String serverDescription, boolean isActive, List<String> authorizedServerIds) {
        return null;
    }

    @Override
    public void deleteAuthorizationServer(String serverId) {

    }
}
