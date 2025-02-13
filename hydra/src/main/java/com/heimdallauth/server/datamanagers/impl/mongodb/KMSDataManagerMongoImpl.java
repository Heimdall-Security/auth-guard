package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.hydra.JWKPrivateModel;
import com.heimdallauth.server.datamanagers.KMSDataManager;
import com.heimdallauth.server.documents.JWKDocument;
import com.heimdallauth.server.services.KeyManagementService;
import com.heimdallauth.server.utils.RandomIdGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class KMSDataManagerMongoImpl implements KMSDataManager {
    private static final String KEY_COLLECTION = "key-collection";
    private final MongoBulkOperationsDAOService mongoBulkOperationsDAOService;
    private final MongoTemplate mongoTemplate;

    public KMSDataManagerMongoImpl(MongoBulkOperationsDAOService mongoBulkOperationsDAOService, MongoTemplate mongoTemplate) {
        this.mongoBulkOperationsDAOService = mongoBulkOperationsDAOService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void storeJWK(String keyId, String encryptedJWK, String encryptedJWKThumbprint, String keyType) {
        JWKDocument document = JWKDocument.builder()
                .id(keyId)
                .encryptedJWK(encryptedJWK)
                .encryptedJWKThumbprint(encryptedJWKThumbprint)
                .keyType(KeyManagementService.KeyType.valueOf(keyType))
                .build();
        List<String> storedKeyIDs = mongoBulkOperationsDAOService.executeMongoDBSaveOperation(List.of(document), KEY_COLLECTION);
        log.info("Stored JWK with ID: {}", storedKeyIDs.getFirst());
    }

    @Override
    public JWKPrivateModel getJWK(String keyId) {
        Query selectKeyById = Query.query(Criteria.where("id").is(keyId));
        JWKDocument document = Optional.ofNullable(this.mongoTemplate.findOne(selectKeyById, JWKDocument.class, KEY_COLLECTION)).orElseThrow(() -> new RuntimeException("Key not found"));
        return JWKPrivateModel.builder()
                .creationTimestamp(document.getCreationTimestamp())
                .encryptedJWK(document.getEncryptedJWK())
                .encryptedJWKThumbprint(document.getEncryptedJWKThumbprint())
                .keyType(document.getKeyType().name())
                .id(document.getId())
                .build();
    }

    @Override
    public void deleteJWK(String keyId) {

    }

    @Override
    public List<JWKPrivateModel> getAllJWKs() {
        Query selectAllJWKsQuery = Query.query(Criteria.where("id").exists(true));
        List<JWKDocument> jwkDocuments = mongoTemplate.find(selectAllJWKsQuery, JWKDocument.class, KEY_COLLECTION);
        return jwkDocuments.stream().map(jwkDocument -> JWKPrivateModel.builder()
                .id(jwkDocument.getId())
                .encryptedJWK(jwkDocument.getEncryptedJWK())
                .encryptedJWKThumbprint(jwkDocument.getEncryptedJWKThumbprint())
                .keyType(jwkDocument.getKeyType().name())
                .build()).toList();
    }
}
