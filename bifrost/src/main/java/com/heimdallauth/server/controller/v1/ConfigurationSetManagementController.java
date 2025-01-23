package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.service.ConfigurationSetManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/configuration-set-management")
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

}
