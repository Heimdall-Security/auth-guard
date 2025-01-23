package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.dto.bifrost.CreateEmailSuppressionListDTO;
import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.commons.models.bifrost.EmailSuppressionList;
import com.heimdallauth.server.service.ConfigurationSetManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/configuration-set-management")
@Tag(name = "Configuration Set Management", description = "APIs for managing configuration sets")
public class ConfigurationSetManagementController {
    private final ConfigurationSetManagementService configurationSetManagementService;

    @Autowired
    public ConfigurationSetManagementController(ConfigurationSetManagementService configurationSetManagementService) {
        this.configurationSetManagementService = configurationSetManagementService;
    }
    @PostMapping
    public ResponseEntity<ConfigurationSet> createNewConfigurationSet(@RequestBody ConfigurationSet configurationSet) {
        return ResponseEntity.ok(configurationSetManagementService.createConfigurationSet(configurationSet));
    }
    @GetMapping("/{configurationSetId}")
    public ResponseEntity<ConfigurationSet> getConfigurationSetById(@PathVariable String configurationSetId) {
        return ResponseEntity.ok(configurationSetManagementService.getConfigurationSetById(configurationSetId));
    }
    @GetMapping
    public ResponseEntity<List<ConfigurationSet>> getAllConfigurationSets(){
        return ResponseEntity.ok(configurationSetManagementService.getAllConfigurationSets());
    }
    @PutMapping("/{configurationSetId}/add-suppression-list/{suppressionListId}")
    public ResponseEntity<ConfigurationSet> updateAddSuppressionListToConfigurationSet(
            @PathVariable("configurationSetId") String configurationSetId,
            @PathVariable("suppressionListId") String suppressionListId) {
        return ResponseEntity.ok(configurationSetManagementService.addSuppressionListToConfigurationSet(configurationSetId, suppressionListId));
    }
    @PostMapping("/suppression-lists")
    public ResponseEntity<EmailSuppressionList> createNewEmailSuppressionList(@RequestBody CreateEmailSuppressionListDTO payloadCreateEmailSuppressionList) {
        return ResponseEntity.ok(configurationSetManagementService.createEmailSuppressionList(payloadCreateEmailSuppressionList));
    }

}
