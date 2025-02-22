package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.hydra.AuthorizationServerModel;
import com.heimdallauth.server.config.HeimdallHydraConfiguration;
import com.heimdallauth.server.datamanagers.AuthorizationServerDataManager;
import com.heimdallauth.server.documents.AuthorizationServerDocument;
import com.heimdallauth.server.utils.RandomIdGeneratorUtil;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    @CacheEvict(value ="authorizationServerCache", key = "'allservers'") //Clear the all servers cache.
    public AuthorizationServerModel createAuthorizationServer(String serverName, String serverDescription, boolean isActive, List<String> authorizedServerIds, String signingKeyId) {
        String serverId = RandomIdGeneratorUtil.generateRandomizedAlphaNumericId();
        AuthorizationServerDocument authorizationServerDocument = AuthorizationServerDocument.builder()
                .id(serverId)
                .authorizationServerName(serverName)
                .authorizationServerDescription(serverDescription)
                .issueUrl(heimdallHydraConfiguration.getIssuerUrl(serverId))
                .isActive(isActive)
                .authorizedServerIds(authorizedServerIds)
                .signingKeyId(signingKeyId)
                .build();
        String savedServerId = executeDbSaveOperation(List.of(authorizationServerDocument), AUTHORIZATION_SERVERS_COLLECTION_NAME).getFirst();
        return Optional.ofNullable(this.mongoTemplate.findById(savedServerId, AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME)).map(AuthorizationServerDocument::toAuthorizationServerModel).orElseThrow(() -> new RuntimeException("Authorization Server not found"));
    }

    @Override
    @Cacheable(value = "authorizationServerCache", key="#serverId", unless = "#result == null")
    public AuthorizationServerModel getAuthorizationServerById(String serverId) {
        log.info("Triggering DB call to get Authorization Server with id: {}", serverId);
        AuthorizationServerDocument authorizationServerDocument = getAuthorizationServerDocumentById(serverId);
        return authorizationServerDocument.toAuthorizationServerModel();
    }

    private AuthorizationServerDocument getAuthorizationServerDocumentById(String serverId){
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(serverId));
        return Optional.ofNullable(mongoTemplate.findOne(query, AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME)).orElseThrow(() -> new RuntimeException("Authorization Server not found"));
    }

    @Override
    @Cacheable(value = "authorizationServerCache", key="'allservers'")
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
    @Cacheable(value ="authorizationServerCache", key = "#serverIds", unless = "#result == null")
    public List<AuthorizationServerModel> getAuthorizationServersByIds(List<String> serverIds) {
        Set<String> serverIdsSet = new HashSet<>(serverIds); //remove duplicated ids.
        Query authorizationServersByIds = Query.query(Criteria.where("id").in(serverIdsSet));
        List<AuthorizationServerDocument> authorizationServerDocuments = mongoTemplate.find(authorizationServersByIds, AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        return authorizationServerDocuments.stream().map(AuthorizationServerDocument::toAuthorizationServerModel).toList();
    }

    @Override
    @CacheEvict(value = "authorizationServerCache", key = "#authorizationServerId", beforeInvocation = true)
    public AuthorizationServerModel updateSigningKeyId(String authorizationServerId, String signingKeyId) {
        AuthorizationServerDocument document = getAuthorizationServerDocumentById(authorizationServerId);
        String oldSigningKeyId = document.getSigningKeyId();
        Update updateSpec= new Update();
        updateSpec.set("signingKeyId", signingKeyId);
        updateSpec.set("legacySigningKeyId", oldSigningKeyId);
        Query authorizationServerById = Query.query(Criteria.where("id").is(authorizationServerId));
        mongoTemplate.updateFirst(authorizationServerById, updateSpec, AuthorizationServerDocument.class, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        AuthorizationServerDocument updatedAuthorizationServer = this.getAuthorizationServerDocumentById(authorizationServerId);
        return updatedAuthorizationServer.toAuthorizationServerModel();
    }

    @Override
    @CacheEvict(value = "authorizationServerCache", key = "#serverId", beforeInvocation = true)
    public void updateAuthorizationServer(String serverId, String serverName, String serverDescription, boolean isActive, List<String> authorizedServerIds) {
        Update updateSpec = new Update();
        updateSpec.set("authorizationServerName", serverName);
        updateSpec.set("authorizationServerDescription", serverDescription);
        updateSpec.set("isActive", isActive);
        updateSpec.set("authorizedServerIds", authorizedServerIds);
    }

    @Override
    public void deleteAuthorizationServer(String serverId) {
        //TODO: Trigger a cleanup for the servers which refer to the deleted authorization servers.
        Query authorizationServerById = Query.query(Criteria.where("id").is(serverId));
        DeleteResult mongoDeleteResult = this.mongoBulkOperationsDAOService.executeBulkMongoDeleteOperation(authorizationServerById, AUTHORIZATION_SERVERS_COLLECTION_NAME);
        log.debug("Deleted Authorization Server with id: {} and count: {}", serverId, mongoDeleteResult.getDeletedCount());
    }
}
