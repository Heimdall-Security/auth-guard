package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.dto.bifrost.SendEmailPayload;
import com.heimdallauth.server.commons.models.bifrost.TemplateModel;
import com.heimdallauth.server.datamanagers.TemplateDataManager;
import com.heimdallauth.server.exceptions.TemplateDoesNotExists;
import com.heimdallauth.server.models.ProcessedEmailBodyModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@Slf4j
public class SendEmailProcessor {
    private final TemplateDataManager templateDM;
    private final EmailTemplateProcessor emailTemplateProcessor;

    @Autowired
    public SendEmailProcessor(TemplateDataManager templateDM, EmailTemplateProcessor emailTemplateProcessor) {
        this.templateDM = templateDM;
        this.emailTemplateProcessor = emailTemplateProcessor;
    }

    public void processSendEmailApiPayload(SendEmailPayload sendEmail){
        log.debug("Processing send email payload: {}", sendEmail);
        if(sendEmail.getTemplate() != null){
            TemplateModel template = sendEmail.getTemplate();
            if(template.getId() !=null || template.getTemplateName() != null) {
                this.processByFetchingFromDataStore(template.getId() ==null ? template.getTemplateName() : template.getId(), sendEmail);
            }else if(template.getTemplateHtmlContent() != null || template.getTemplatePlaintextContent() != null || template.getTemplateSubject() != null){
                assert template.getTemplateSubject() != null;
                assert template.getTemplatePlaintextContent() != null;
                assert template.getTemplateHtmlContent() != null;
                this.processInlineTemplate(template, sendEmail.getData());
            }
        }
    }
    private void processByFetchingFromDataStore(String templateId, SendEmailPayload sendEmail){
        TemplateModel template;
        log.info("Processing send email payload by fetching from data store: {}", sendEmail);
        try{
            String validatedUUID =  UUID.fromString(templateId).toString();
            template = this.templateDM.getTemplateById(validatedUUID);
        }catch (IllegalArgumentException e){
            log.debug("Template ID is not a valid UUID: {}", templateId);
            template = this.templateDM.getTemplateByName(templateId);
        }catch (TemplateDoesNotExists e){
            log.error("Template does not exists: {}", templateId);
            return;
        }
        this.processInitiateEmailTemplating(template, sendEmail.getData());
    }
    private void processInlineTemplate(TemplateModel inlineTemplateModel, Object data){
        log.info("Processing inline template: {}", inlineTemplateModel);
        this.processInitiateEmailTemplating(inlineTemplateModel, data);
    }

    private void processInitiateEmailTemplating(TemplateModel template, Object data){
        log.info("Processing initiate email templating: {}", template);
        ProcessedEmailBodyModel processedEmailBody = this.emailTemplateProcessor.processEmailTemplate(template, convertDataToMap(data));
    }

    private void executeEmailSend(ProcessedEmailBodyModel processedEmailBody, SendEmailPayload sendEmail){
        log.info("Executing email send: {}", sendEmail);
        //TODO implement the service call to the JavaMailSender
    }

    private static Map<String, Object> convertDataToMap(Object data){
        Map<String, Object> resultMap = new HashMap<>();
        if(data == null){
            return Collections.emptyMap();
        }
        Field[] declaredFields = data.getClass().getDeclaredFields();
        for (Field field: declaredFields){
            ReflectionUtils.makeAccessible(field);
            try{
                resultMap.put(field.getName(), field.get(data));
            } catch (IllegalAccessException e) {
                log.error("Failed to convert data to map : {}, errorMessage: {}", field,e.getMessage());
            }
        }
        return resultMap;
    }
}
