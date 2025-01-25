package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;

import java.util.List;
import java.util.Optional;

public interface ConfigurationSetDataManager {
    ConfigurationSet createConfigurationSet(ConfigurationSet configurationSet);
    Optional<ConfigurationSet> getConfigurationSet(String configurationSetName);
    void deleteConfigurationSet(String configurationSetId);
    List<ConfigurationSet> getAllConfigurationSets();
    Optional<ConfigurationSet> addSuppressionListToConfigurationSet(String configurationSetId, String suppressionListId);
}
