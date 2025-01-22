package com.heimdallauth.server.datamanagers.impl.mongodb;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.datamanagers.ConfigurationSetDataManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class ConfigurationSetDataManagerMongoImpl implements ConfigurationSetDataManager {
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
}
