package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.bifrost.TemplateModel;
import com.heimdallauth.server.constants.MongoCollectionConstants;
import com.heimdallauth.server.datamanagers.TemplateDataManager;
import com.heimdallauth.server.documents.TemplateDocument;
import com.heimdallauth.server.exceptions.TemplateDoesNotExists;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class TemplateDataManagerMongoImpl implements TemplateDataManager {
    private final MongoTemplate mongoTemplate;
    private final MongoBulkOperationsService mongoBulkOperationsService;


    public TemplateDataManagerMongoImpl(MongoTemplate mongoTemplate, MongoBulkOperationsService mongoBulkOperationsService) {
        this.mongoTemplate = mongoTemplate;
        this.mongoBulkOperationsService = mongoBulkOperationsService;
    }

    private <T> List<String> executeMongoDBSaveOperation(List<T> objectCollection, String collectionName) {
        return this.mongoBulkOperationsService.executeMongoDBSaveOperation(objectCollection, collectionName);
    }

    @Override
    public TemplateModel createTemplate(String templateName, String templateHtmlContent, String templatePlaintextContent, String templateSubject) {
        TemplateDocument templateDocument  = TemplateDocument.builder()
                .id(UUID.randomUUID().toString())
                .templateName(templateName)
                .templateHtmlContent(templateHtmlContent)
                .templatePlaintextContent(templatePlaintextContent)
                .templateSubject(templateSubject)
                .createdAt(Instant.now())
                .lastUpdatedAt(Instant.now())
                .build();
        List<String> savedIds = this.executeMongoDBSaveOperation(List.of(templateDocument), MongoCollectionConstants.TEMPLATE_COLLECTION);
        if (!savedIds.isEmpty()) {
            return this.getTemplateById(savedIds.getFirst());
        }else{
            throw new RuntimeException("Error saving template");
        }
    }

    @Override
    public TemplateModel getTemplateById(String id) {
        Optional<TemplateDocument> templateDocument = this.getTemplateDocumentById(id);
        return templateDocument.map(TemplateDocument::toTemplateModel).orElseThrow();
    }

    @Override
    public TemplateModel getTemplateByName(String templateName) {
        Query selectTemplateByNameQuery = Query.query(Criteria.where("templateName").is(templateName));
        Optional<TemplateDocument> templateDocument = Optional.ofNullable(this.mongoTemplate.findOne(selectTemplateByNameQuery, TemplateDocument.class, MongoCollectionConstants.TEMPLATE_COLLECTION));
        return templateDocument.map(TemplateDocument::toTemplateModel).orElseThrow(()-> new TemplateDoesNotExists("Template with name " + templateName + " does not exist"));
    }

    @Override
    public TemplateModel updateTemplate(String id, String templateName, String templateHtmlContent, String templatePlaintextContent, String templateSubject) {
        Optional<TemplateDocument> templateDocument = this.getTemplateDocumentById(id);
        templateDocument.ifPresentOrElse(
                document -> {
                    document.setTemplateName(templateName);
                    document.setTemplateHtmlContent(templateHtmlContent);
                    document.setTemplatePlaintextContent(templatePlaintextContent);
                    document.setTemplateSubject(templateSubject);
                    document.setLastUpdatedAt(Instant.now());
                    this.mongoTemplate.save(document, MongoCollectionConstants.TEMPLATE_COLLECTION);
                },
                () -> {
                    throw new RuntimeException("Template not found");
                }
        );
        return this.getTemplateById(id);
    }

    @Override
    public void deleteTemplate(String id) {
        Query selectById = Query.query(Criteria.where("id").is(id));
        DeleteResult deleteResult = this.mongoTemplate.remove(selectById, TemplateDocument.class, MongoCollectionConstants.TEMPLATE_COLLECTION);
        if(deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() == 0){
            throw new RuntimeException("Template not found");
        }

    }

    @Override
    public List<TemplateModel> getAllTemplates() {
        List<TemplateDocument> templateDocuments = this.mongoTemplate.findAll(TemplateDocument.class, MongoCollectionConstants.TEMPLATE_COLLECTION);
        return templateDocuments.stream().map(TemplateDocument::toTemplateModel).toList();
    }

    private Optional<TemplateDocument> getTemplateDocumentById(String id){
        Query selectTemplateByIdQuery = Query.query(Criteria.where("id").is(id));
        return Optional.ofNullable(this.mongoTemplate.findOne(selectTemplateByIdQuery, TemplateDocument.class, MongoCollectionConstants.TEMPLATE_COLLECTION));
    }
}
