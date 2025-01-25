package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.commons.models.bifrost.EmailSuppressionList;
import com.heimdallauth.server.commons.models.bifrost.SuppressionListEmailEntry;
import com.heimdallauth.server.datamanagers.impl.mongodb.ConfigurationSetDataManagerMongoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ConfigurationSetValidationService {

    private final ConfigurationSetDataManagerMongoImpl configurationSetDataManager;

    @Autowired
    public ConfigurationSetValidationService(ConfigurationSetDataManagerMongoImpl configurationSetDataManager) {
        this.configurationSetDataManager = configurationSetDataManager;
    }

    public void validateConfigurationSet(ConfigurationSet configurationSet) {
        validateUniqueConfigurationSet(configurationSet.getConfigurationSetName());
        validateCustomRedirectDomain(configurationSet.getTrackingOptions().getCustomTrackingDomain());
        validateEmailFormat(configurationSet.getActiveEmailSuppressionLists());
    }

    private void validateUniqueConfigurationSet(String configurationSetName) {
        Optional<ConfigurationSet> existingConfigurationSet = configurationSetDataManager.getConfigurationSet(configurationSetName);
        if (existingConfigurationSet.isPresent()) {
            throw new IllegalArgumentException("Configuration set with name " + configurationSetName + " already exists");
        }
    }

    private void validateCustomRedirectDomain(String customRedirectDomain) {
        if (customRedirectDomain != null && !customRedirectDomain.isEmpty()) {
            String domainRegex = "^(https?:\\/\\/)?([\\da-z.-]+)\\.([a-z.]{2,6})([\\/\\w .-]*)*\\/?$";
            if (!Pattern.matches(domainRegex, customRedirectDomain)) {
                throw new IllegalArgumentException("Invalid custom redirect domain: " + customRedirectDomain);
            }
        }
    }

    private void validateEmailFormat(List<EmailSuppressionList> emailSuppressionLists) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        for (EmailSuppressionList emailSuppressionList : emailSuppressionLists) {
            for (SuppressionListEmailEntry emailEntry : emailSuppressionList.getEmailSuppressions()) {
                if (!pattern.matcher(emailEntry.getEmailAddress()).matches()) {
                    throw new IllegalArgumentException("Invalid email format: " + emailEntry.getEmailAddress());
                }
            }
        }
    }
}