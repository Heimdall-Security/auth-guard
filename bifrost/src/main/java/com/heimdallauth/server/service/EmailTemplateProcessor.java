package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.models.TemplateModel;
import com.heimdallauth.server.datamanagers.TemplateDataManager;
import com.heimdallauth.server.models.ProcessedEmailBodyModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EmailTemplateProcessor {
    private final TemplateRenderService templateRenderService;
    private final TemplateDataManager templateDataManager;

    @Autowired
    public EmailTemplateProcessor(TemplateRenderService templateRenderService, TemplateDataManager templateDataManager) {
        this.templateRenderService = templateRenderService;
        this.templateDataManager = templateDataManager;
    }
    public ProcessedEmailBodyModel processEmailTemplateByTemplateName(String templateName, Map<String, Object> variablesValueMap){
        TemplateModel template = templateDataManager.getTemplateByName(templateName);
        return processEmailTemplate(template, variablesValueMap);
    }
    public ProcessedEmailBodyModel processEmailTemplateById(String templateId, Map<String, Object> variablesValueMap){
        TemplateModel template = templateDataManager.getTemplateById(templateId);
        return processEmailTemplate(template, variablesValueMap);
    }

    private ProcessedEmailBodyModel processEmailTemplate(TemplateModel template, Map<String, Object> variablesValueMap){
        String processedSubjectString = templateRenderService.renderTemplatedString(template.getTemplateSubject(), variablesValueMap);
        String processedHtmlBodyString = templateRenderService.renderTemplatedString(template.getTemplateHtmlContent(), variablesValueMap);
        String processedTextBodyString = templateRenderService.renderTemplatedString(template.getTemplatePlaintextContent(), variablesValueMap);
        return ProcessedEmailBodyModel.builder()
                .subject(processedSubjectString)
                .htmlProcessedBody(processedHtmlBodyString)
                .textProcessedBody(processedTextBodyString)
                .build();
    }
}
