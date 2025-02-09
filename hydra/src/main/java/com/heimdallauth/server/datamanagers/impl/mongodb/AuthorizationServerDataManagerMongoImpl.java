package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;
import com.heimdallauth.server.config.HeimdallHydraConfiguration;
import com.heimdallauth.server.datamanagers.AuthorizationServerDataManager;
import com.heimdallauth.server.documents.AuthorizationServerDocument;
import com.heimdallauth.server.utils.RandomIdGeneratorUtil;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
public class AuthorizationServerDataManagerMongoImpl implements AuthorizationServerDataManager {
    private static final String AUTHORIZATION_SERVERS_COLLECTION_NAME = "authorization_servers";

    private final MongoTemplate mongoTemplate;
    private final MongoBulkOperationsDAOService mongoBulkOperationsDAOService;
    private final HeimdallHydraConfiguration heimdallHydraConfiguration;

    @Autowired
    public AuthorizationServerDataManagerMongoImpl(MongoTemplate mongoTemplate, MongoBulkOperationsDAOService mongoBulkOperationsDAOService, HeimdallHydraConfiguration heimdallHydraConfiguration) {
        this.mongoTemplate = mongoTemplate;
        this.mongoBulkOperationsDAOService = mongoBulkOperationsDAOService;
        this.heimdallHydraConfiguration = heimdallHydraConfiguration;
    }

    private <T> List<String> executeDbSaveOperation(List<T> documentsToSave, String collectionName){
        return this.mongoBulkOperationsDAOService.executeMongoDBSaveOperation(documentsToSave, collectionName);
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
        String savedServerId = executeDbSaveOperation(List.of(authorizationServerDocument), AUTHORIZATION_SERVERS_COLLECTION_NAME).getFirst();
        return getAuthorizationServerById(savedServerId);
    }

    @Override
    public AuthorizationServerModel getAuthorizationServerById(String serverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(serverId));
        AuthorizationServerDocument authorizationServerDocument = Optional.ofNullable(mongoTemplate.findOne(query, AuthorizationServerDocument.class)).orElseThrow(() -> new RuntimeException("Authorization Server not found"));
        return authorizationServerDocument.toAuthorizationServerModel();
    }

    @Override
    public List<AuthorizationServerModel> getAuthorizationServers() {
        List<AuthorizationServerDocument> authorizationServerDocuments = mongoTemplate.findAll(AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        return authorizationServerDocuments.stream().map(AuthorizationServerDocument::toAuthorizationServerModel).toList();
    }

    @Override
    public List<AuthorizationServerModel> getActiveAuthorizationServers() {
        Query authorizationServersActive = Query.query(Criteria.where("isActive").is(true));
        List<AuthorizationServerDocument> authorizationServerDocuments = mongoTemplate.find(authorizationServersActive, AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        return authorizationServerDocuments.stream().map(AuthorizationServerDocument::toAuthorizationServerModel).toList();
    }

    @Override
    public List<AuthorizationServerModel> getInactiveAuthorizationServers() {
        Query authorizationServersActive = Query.query(Criteria.where("isActive").is(false));
        List<AuthorizationServerDocument> authorizationServerDocuments = mongoTemplate.find(authorizationServersActive, AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        return authorizationServerDocuments.stream().map(AuthorizationServerDocument::toAuthorizationServerModel).toList();
    }

    /*
    Possible race condition (when authorized server ids are cascaded) not required to be handled now but will need to be handled in Aggregation Pipelines. - not handled
     */
    @Override
    public List<AuthorizationServerModel> getAuthorizationServersByIds(List<String> serverIds) {
        Set<String> serverIdsSet = new HashSet<>(serverIds); //remove duplicated ids.
        Query authorizationServersByIds = Query.query(Criteria.where("id").in(serverIdsSet));
        List<AuthorizationServerDocument> authorizationServerDocuments = mongoTemplate.find(authorizationServersByIds, AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        return authorizationServerDocuments.stream().map(AuthorizationServerDocument::toAuthorizationServerModel).toList();
    }

    @Override
    public AuthorizationServerModel updateAuthorizationServer(String serverId, String serverName, String serverDescription, boolean isActive, List<String> authorizedServerIds) {
        return null;
    }

    @Override
    public void deleteAuthorizationServer(String serverId) {
        //TODO: Trigger a cleanup for the servers which refer to the deleted authorization servers.
        Query authorizationServerById = Query.query(Criteria.where("id").is(serverId));
        DeleteResult mongoDeleteResult = this.mongoBulkOperationsDAOService.executeBulkMongoDeleteOperation(authorizationServerById, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        log.debug("Deleted Authorization Server with id: {} and count: {}", serverId, mongoDeleteResult.getDeletedCount());
    }
}
