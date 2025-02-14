package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.dto.bifrost.CreateEmailSuppressionListDTO;
import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.commons.models.bifrost.EmailSuppressionList;
import com.heimdallauth.server.datamanagers.ConfigurationSetDataManager;
import com.heimdallauth.server.datamanagers.EmailSuppressionListDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConfigurationSetManagementService {
    private final ConfigurationSetDataManager configurationSetDataManager;
    private final EmailSuppressionListDataManager emailSuppressionListDataManager;
    private final ConfigurationSetValidationService configurationSetValidationService;

    @Autowired
    public ConfigurationSetManagementService(ConfigurationSetDataManager configurationSetDataManager, EmailSuppressionListDataManager emailSuppressionListDataManager, ConfigurationSetValidationService configurationSetValidationService) {
        this.configurationSetDataManager = configurationSetDataManager;
        this.emailSuppressionListDataManager = emailSuppressionListDataManager;
        this.configurationSetValidationService = configurationSetValidationService;
    }

    public ConfigurationSet createConfigurationSet(ConfigurationSet configurationSet) {
        this.configurationSetValidationService.validateConfigurationSet(configurationSet);
        return configurationSetDataManager.createConfigurationSet(configurationSet);
    }
    public ConfigurationSet getConfigurationSetById(String configurationSetId) {
        return configurationSetDataManager.getConfigurationSet(configurationSetId).orElse(null);
    }
    public List<ConfigurationSet> getAllConfigurationSets() {
        return configurationSetDataManager.getAllConfigurationSets();
    }
    public ConfigurationSet addSuppressionListToConfigurationSet(String configurationSetId, String suppressionListId) {
        return configurationSetDataManager.addSuppressionListToConfigurationSet(configurationSetId, suppressionListId).orElse(null);
    }
    public EmailSuppressionList createEmailSuppressionList(CreateEmailSuppressionListDTO payloadCreateEmailSuppressionList){
        return emailSuppressionListDataManager.createEmailSuppressionList(
                payloadCreateEmailSuppressionList.getSuppressionListName(),
                payloadCreateEmailSuppressionList.getEmailAddresses(),
                payloadCreateEmailSuppressionList.isEmailBlocked()
        );
    }
}
