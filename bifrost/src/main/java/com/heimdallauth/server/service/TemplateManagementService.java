package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.models.bifrost.TemplateModel;
import com.heimdallauth.server.datamanagers.TemplateDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TemplateManagementService {
    private final TemplateDataManager templateDataManager;
    private final TemplateValidationService templateValidationService;

    public TemplateManagementService(TemplateDataManager templateDataManager, TemplateValidationService templateValidationService) {
        this.templateDataManager = templateDataManager;
        this.templateValidationService = templateValidationService;
    }

    public TemplateModel createNewTemplate(TemplateModel templateModel){
        this.templateValidationService.validateTemplate(templateModel);
        return this.createNewTemplate(
            templateModel.getTemplateName(),
            templateModel.getTemplateHtmlContent(),
            templateModel.getTemplatePlaintextContent(),
            templateModel.getTemplateSubject()
        );
    }
    public TemplateModel createNewTemplate(String templateName, String templateHtmlContent, String templatePlaintextContent, String templateSubject){
        return this.templateDataManager.createTemplate(templateName, templateHtmlContent, templatePlaintextContent, templateSubject);
    }
    public TemplateModel getTemplateById(String id){
        return this.templateDataManager.getTemplateById(id);
    }
    public TemplateModel getTemplateByName(String templateName){
        return this.templateDataManager.getTemplateByName(templateName);
    }
    public TemplateModel updateTemplate(String id, TemplateModel templateModel){
        return this.templateDataManager.updateTemplate(
            id,
            templateModel.getTemplateName(),
            templateModel.getTemplateHtmlContent(),
            templateModel.getTemplatePlaintextContent(),
            templateModel.getTemplateSubject()
        );
    }
    public List<TemplateModel> getAllTemplates(){
        return this.templateDataManager.getAllTemplates();
    }
    public void deleteTemplate(String id){
        this.templateDataManager.deleteTemplate(id);
    }
}
