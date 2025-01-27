package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.models.TemplateModel;
import com.heimdallauth.server.datamanagers.TemplateDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TemplateManagementService {
    private final TemplateDataManager templateDataManager;

    public TemplateManagementService(TemplateDataManager templateDataManager) {
        this.templateDataManager = templateDataManager;
    }

    public TemplateModel createNewTemplate(TemplateModel templateModel){
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
}
