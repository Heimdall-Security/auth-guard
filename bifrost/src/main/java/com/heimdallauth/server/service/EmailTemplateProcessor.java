package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.models.bifrost.TemplateModel;
import com.heimdallauth.server.models.ProcessedEmailBodyModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailTemplateProcessor {
    private final TemplateRenderService templateRenderService;

    @Autowired
    public EmailTemplateProcessor(TemplateRenderService templateRenderService) {
        this.templateRenderService = templateRenderService;
    }


    protected ProcessedEmailBodyModel processEmailTemplate(TemplateModel template, Map<String, Object> variablesValueMap){
        String processedSubjectString = templateRenderService.render(template.getTemplateSubject(), variablesValueMap, TemplateRenderService.TemplateMode.TEXT);
        String processedHtmlBodyString = templateRenderService.render(template.getTemplateHtmlContent(), variablesValueMap, TemplateRenderService.TemplateMode.HTML);
        String processedTextBodyString = templateRenderService.render(template.getTemplatePlaintextContent(), variablesValueMap,TemplateRenderService.TemplateMode.TEXT);
        return ProcessedEmailBodyModel.builder()
                .subject(processedSubjectString)
                .htmlProcessedBody(processedHtmlBodyString)
                .textProcessedBody(processedTextBodyString)
                .build();
    }
}
