package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.commons.models.bifrost.EmailSuppressionList;
import com.heimdallauth.server.constants.MongoCollectionConstants;
import com.heimdallauth.server.datamanagers.ConfigurationSetDataManager;
import com.heimdallauth.server.datamanagers.EmailSuppressionListDataManager;
import com.heimdallauth.server.documents.ConfigurationSetDocument;
import com.heimdallauth.server.documents.EmailSuppressionListDocument;
import com.heimdallauth.server.documents.TrackingOptionsDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;

@Repository
@Slf4j
public class ConfigurationSetDataManagerMongoImpl implements ConfigurationSetDataManager, EmailSuppressionListDataManager {
    private final MongoTemplate mongoTemplate;

    public ConfigurationSetDataManagerMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private String executeSaveEmailSuppressionList(EmailSuppressionList emailSuppressionList) {
        EmailSuppressionListDocument suppressionListDocument = EmailSuppressionListDocument.builder()
                .id(UUID.randomUUID().toString())
                .emailSuppressions(emailSuppressionList.getEmailSuppressions())
                .creationTimestamp(Instant.now())
                .lastUpdateTimestamp(Instant.now())
                .suppressionListName(emailSuppressionList.getSuppressionListName())
                .build();
        return this.executeMongoDBSaveOperation(List.of(suppressionListDocument), MongoCollectionConstants.EMAIL_SUPPRESSION_LIST_COLLECTION).getFirst();
    }

    private <T> List<String> executeMongoDBSaveOperation(List<T> objectCollection, String collectionName) {
        Collection<T> savedCollection = this.mongoTemplate.insert(objectCollection, collectionName);
        List<String> savedCollectionIds = new ArrayList<>();
        log.debug("Saved {} objects to MongoDB collection {}", savedCollection.size(), collectionName);

        if (!savedCollection.isEmpty()) {
            try {
                Method getIdMethod = savedCollection.iterator().next().getClass().getMethod("getId");
                for (T document : savedCollection) {
                    savedCollectionIds.add((String) getIdMethod.invoke(document));
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("Error retrieving IDs from saved documents", e);
            }
        }

        return savedCollectionIds;
    }

    private Optional<ConfigurationSet> getConfigurationSetById(String configurationSetId) {
        Aggregation configurationSetLookupAggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(configurationSetId)),
                Aggregation.lookup(MongoCollectionConstants.EMAIL_SUPPRESSION_LIST_COLLECTION, "activeEmailSuppressionListIds", "_id", "activeEmailSuppressionLists"),
                Aggregation.project()
                        .andExclude("activeEmailSuppressionListIds")
        );
        AggregationResults<ConfigurationSet> configurationSetAggregationResults = this.mongoTemplate.aggregate(configurationSetLookupAggregation, MongoCollectionConstants.CONFIGURATION_SET_COLLECTION, ConfigurationSet.class);
        return Optional.ofNullable(configurationSetAggregationResults.getUniqueMappedResult());
    }
    @Override
    public ConfigurationSet createConfigurationSet(ConfigurationSet configurationSet) {
        String savedTrackingOptionsId = null;
        List<String> activeSuppressionListIds = new ArrayList<>();
        if (configurationSet.getConfigurationSetName() == null) {
            //Configuration set name is required
            throw new IllegalArgumentException("Configuration set name is required");
        }
        if (configurationSet.getTrackingOptions() != null) {
            log.debug("Trigger save tracking options");
            //Tracking options are present and should be saved
            TrackingOptionsDocument trackingOptionsDocument = TrackingOptionsDocument.builder()
                    .customTrackingDomain(configurationSet.getTrackingOptions().getCustomTrackingDomain())
                    .useCustomTrackingDomain(configurationSet.getTrackingOptions().isUseCustomTrackingDomain())
                    .build();
            savedTrackingOptionsId = this.executeMongoDBSaveOperation(List.of(trackingOptionsDocument), MongoCollectionConstants.TRACKING_OPTIONS_COLLECTION).getFirst();
        }
        if (configurationSet.getActiveEmailSuppressionLists() != null) {
            //Email suppression lists if present should be saved
            log.debug("Trigger save active suppression lists");
            List<EmailSuppressionList> activeEmailSuppressionListForConfigurationSet = configurationSet.getActiveEmailSuppressionLists();
            for (EmailSuppressionList emailSuppressionList : activeEmailSuppressionListForConfigurationSet) {
                activeSuppressionListIds.add(this.executeSaveEmailSuppressionList(emailSuppressionList));
            }
        }
        ConfigurationSetDocument configurationSetDocument = ConfigurationSetDocument.builder()
                .id(UUID.randomUUID().toString())
                .configurationSetName(configurationSet.getConfigurationSetName())
                .activeEmailSuppressionListIds(activeSuppressionListIds)
                .trackingOptionsId(savedTrackingOptionsId)
                .build();
        String savedConfigurationSetId = this.executeMongoDBSaveOperation(List.of(configurationSetDocument), MongoCollectionConstants.CONFIGURATION_SET_COLLECTION).getFirst();
        log.debug("Saved configuration set with ID {}", savedConfigurationSetId);
        return this.getConfigurationSetById(savedConfigurationSetId).orElseThrow();
    }

    @Override
    public Optional<ConfigurationSet> getConfigurationSet(String configurationSetName) {
        return Optional.empty();
    }

    @Override
    public void deleteConfigurationSet(String configurationSetId) {

    }

    @Override
    public List<ConfigurationSet> getAllConfigurationSets() {
        return List.of();
    }

    @Override
    public EmailSuppressionList createEmailSuppressionList(String suppressionListName, List<String> emailAddresses, boolean blockDelivery) {
        return null;
    }

    @Override
    public Optional<EmailSuppressionList> getEmailSuppressionListByName(String suppressionListName) {
        return Optional.empty();
    }

    @Override
    public Optional<EmailSuppressionList> getEmailSuppressionListById(String suppressionListId) {
        Aggregation emailSuppressionListAggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(suppressionListId)),
                Aggregation.lookup(MongoCollectionConstants.EMAIL_ENTRY_COLLECTION, "emailSuppressionEntryIds", "_id", "emailSuppressions")
        );
        AggregationResults<EmailSuppressionList> emailSuppressionListAggregationResults = this.mongoTemplate.aggregate(emailSuppressionListAggregation, MongoCollectionConstants.EMAIL_SUPPRESSION_LIST_COLLECTION, EmailSuppressionList.class);
        return Optional.ofNullable(emailSuppressionListAggregationResults.getUniqueMappedResult());
    }

    @Override
    public void deleteEmailSuppressionList(String suppressionListId) {

    }

    @Override
    public List<EmailSuppressionList> getAllEmailSuppressionLists() {
        return List.of();
    }

    @Override
    public EmailSuppressionList addEmailsToSuppressionList(String suppressionListId, List<String> emailAddresses, boolean blockDelivery) {
        return null;
    }

    @Override
    public EmailSuppressionList removeEmailsFromSuppressionList(String suppressionListId, List<String> emailAddresses) {
        return null;
    }

    @Override
    public EmailSuppressionList updateEmailSuppressionList(String suppressionListId, String suppressionListName, List<String> emailAddresses, boolean blockDelivery) {
        return null;
    }
}
