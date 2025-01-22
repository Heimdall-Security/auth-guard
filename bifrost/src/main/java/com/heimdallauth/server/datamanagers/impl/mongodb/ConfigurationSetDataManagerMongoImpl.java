package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.commons.models.bifrost.EmailSuppressionList;
import com.heimdallauth.server.datamanagers.ConfigurationSetDataManager;
import com.heimdallauth.server.datamanagers.EmailSuppressionListDataManager;

import java.util.List;
import java.util.Optional;

public class ConfigurationSetDataManagerMongoImpl implements ConfigurationSetDataManager, EmailSuppressionListDataManager {
    @Override
    public ConfigurationSet createConfigurationSet(ConfigurationSet configurationSet) {
        return null;
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
