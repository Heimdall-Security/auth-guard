package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.models.TemplateModel;
import com.heimdallauth.server.service.TemplateManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/manage/templates")
public class TemplateManagementController {
    private final TemplateManagementService templateManagementService;

    public TemplateManagementController(TemplateManagementService templateManagementService) {
        this.templateManagementService = templateManagementService;
    }

    @PostMapping
    public ResponseEntity<TemplateModel> createNewTemplate(@RequestBody TemplateModel templateCreatePayload){
        return ResponseEntity.ok(templateManagementService.createNewTemplate(templateCreatePayload));
    }
    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateModel> getTemplateById(@PathVariable String templateId){
        return ResponseEntity.ok(templateManagementService.getTemplateById(templateId));
    }
    @GetMapping
    public ResponseEntity<List<TemplateModel>> getAllTemplates(){
        return ResponseEntity.ok(templateManagementService.getAllTemplates());
    }
    @PutMapping("/{templateId}")
    public ResponseEntity<TemplateModel> updateTemplate(@PathVariable String templateId, @RequestBody TemplateModel templateUpdatePayload){
        return ResponseEntity.ok(templateManagementService.updateTemplate(templateId, templateUpdatePayload));
    }
}
