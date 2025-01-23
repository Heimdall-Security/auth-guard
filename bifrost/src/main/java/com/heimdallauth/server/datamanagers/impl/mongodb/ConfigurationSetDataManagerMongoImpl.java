package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.commons.models.bifrost.EmailSuppressionList;
import com.heimdallauth.server.constants.MongoCollectionConstants;
import com.heimdallauth.server.datamanagers.ConfigurationSetDataManager;
import com.heimdallauth.server.datamanagers.EmailSuppressionListDataManager;
import com.heimdallauth.server.documents.ConfigurationSetDocument;
import com.heimdallauth.server.documents.EmailSuppressionListDocument;
import com.heimdallauth.server.documents.SuppressionListEntryDocument;
import com.heimdallauth.server.documents.TrackingOptionsDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Repository
@Slf4j
public class ConfigurationSetDataManagerMongoImpl implements ConfigurationSetDataManager, EmailSuppressionListDataManager {
    private final MongoTemplate mongoTemplate;

    public ConfigurationSetDataManagerMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private EmailSuppressionList executeSaveEmailSuppressionList(EmailSuppressionList emailSuppressionList) {
        List<SuppressionListEntryDocument> emailSuppressions = emailSuppressionList.getEmailSuppressions().stream().map(suppressionListEntry -> SuppressionListEntryDocument.builder()
                .id(UUID.randomUUID().toString())
                .emailAddress(suppressionListEntry.getEmailAddress())
                .isEmailDeliveryBlocked(suppressionListEntry.isEmailDeliveryBlocked())
                .build()).toList();
        List<String> emailSuppressionEntriesIds = this.executeMongoDBSaveOperation(emailSuppressions, MongoCollectionConstants.EMAIL_ENTRY_COLLECTION);
        EmailSuppressionListDocument emailSuppressionListDocument = EmailSuppressionListDocument.builder()
                .id(UUID.randomUUID().toString())
                .suppressionListName(emailSuppressionList.getSuppressionListName())
                .emailSuppressionEntryIds(emailSuppressionEntriesIds)
                .creationTimestamp(emailSuppressionList.getCreationTimestamp())
                .lastUpdateTimestamp(emailSuppressionList.getLastUpdateTimestamp())
                .build();
        String savedEmailSuppressionListId = this.executeMongoDBSaveOperation(List.of(emailSuppressionListDocument), MongoCollectionConstants.EMAIL_SUPPRESSION_LIST_COLLECTION).getFirst();
        return this.getEmailSuppressionListById(savedEmailSuppressionListId).orElseThrow();
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
        return Optional.empty();
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
            //Tracking options are present and should be saved
            TrackingOptionsDocument trackingOptionsDocument = TrackingOptionsDocument.builder()
                    .customTrackingDomain(configurationSet.getTrackingOptions().getCustomTrackingDomain())
                    .useCustomTrackingDomain(configurationSet.getTrackingOptions().isUseCustomTrackingDomain())
                    .build();
            savedTrackingOptionsId = this.executeMongoDBSaveOperation(List.of(trackingOptionsDocument), MongoCollectionConstants.TRACKING_OPTIONS_COLLECTION).getFirst();
        }
        if (configurationSet.getActiveEmailSuppressionLists() != null) {
            //Email suppression lists if present should be saved
            List<EmailSuppressionList> activeEmailSuppressionListForConfigurationSet = configurationSet.getActiveEmailSuppressionLists();
            for (EmailSuppressionList emailSuppressionList : activeEmailSuppressionListForConfigurationSet) {
                EmailSuppressionList savedEmailSuppressionList = this.executeSaveEmailSuppressionList(emailSuppressionList);
                activeSuppressionListIds.add(savedEmailSuppressionList.getId());
            }
        }
        ConfigurationSetDocument configurationSetDocument = ConfigurationSetDocument.builder()
                .configurationSetName(configurationSet.getConfigurationSetName())
                .activeEmailSuppressionLists(activeSuppressionListIds)
                .trackingOptionsId(savedTrackingOptionsId)
                .build();
        String savedConfigurationSetId = this.executeMongoDBSaveOperation(List.of(configurationSetDocument), MongoCollectionConstants.CONFIGURATION_SET_COLLECTION).getFirst();
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
        return Optional.empty();
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
