package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.dto.bifrost.SendEmailPayload;
import com.heimdallauth.server.commons.models.bifrost.RecipientModel;
import com.heimdallauth.server.commons.models.bifrost.TemplateModel;
import com.heimdallauth.server.configuration.HeimdallSmtpConfiguration;
import com.heimdallauth.server.datamanagers.TemplateDataManager;
import com.heimdallauth.server.exceptions.TemplateDoesNotExists;
import com.heimdallauth.server.models.ProcessedEmailBodyModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotEmpty;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@Slf4j
public class SendEmailProcessor {
    private final TemplateDataManager templateDM;
    private final EmailTemplateProcessor emailTemplateProcessor;
    private final JavaMailSender javaMailSender;
    private final HeimdallSmtpConfiguration smtpConfiguration;

    @Autowired
    public SendEmailProcessor(TemplateDataManager templateDM, EmailTemplateProcessor emailTemplateProcessor, JavaMailSender javaMailSender, HeimdallSmtpConfiguration smtpConfiguration) {
        this.templateDM = templateDM;
        this.emailTemplateProcessor = emailTemplateProcessor;
        this.javaMailSender = javaMailSender;
        this.smtpConfiguration = smtpConfiguration;
    }

    public void processSendEmailApiPayload(SendEmailPayload sendEmail) throws MessagingException {
        log.debug("Processing send email payload: {}", sendEmail);
        if(sendEmail.getTemplate() != null){
            TemplateModel template = sendEmail.getTemplate();
            if(template.getId() !=null || template.getTemplateName() != null) {
                this.processByFetchingFromDataStore(template.getId() ==null ? template.getTemplateName() : template.getId(), sendEmail);
            }else if(template.getTemplateHtmlContent() != null || template.getTemplatePlaintextContent() != null || template.getTemplateSubject() != null){
                assert template.getTemplateSubject() != null;
                assert template.getTemplatePlaintextContent() != null;
                assert template.getTemplateHtmlContent() != null;
                this.processInlineTemplate(sendEmail, sendEmail.getData());
            }
        }
    }
    private void processByFetchingFromDataStore(String templateId, SendEmailPayload sendEmail) throws MessagingException {
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
        ProcessedEmailBodyModel processedEmailBodyModel = this.processInitiateEmailTemplating(template, sendEmail.getData());
        this.executeEmailSend(processedEmailBodyModel, sendEmail);

    }
    private void processInlineTemplate(SendEmailPayload payload, Object data) throws MessagingException {
        log.info("Processing inline template: {}", payload.getTemplate());
        ProcessedEmailBodyModel processedEmailBodyModel = this.processInitiateEmailTemplating(payload.getTemplate(), data);
        this.executeEmailSend(processedEmailBodyModel, payload);

    }

    private ProcessedEmailBodyModel processInitiateEmailTemplating(TemplateModel template, Object data){
        log.info("Processing initiate email templating: {}", template);
        return this.emailTemplateProcessor.processEmailTemplate(template, convertDataToMap(data));
    }

    private static Map<String, Object> convertDataToMap(Object data) {
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

    private void executeEmailSend(ProcessedEmailBodyModel processedEmailBody, SendEmailPayload sendEmail) throws MessagingException {
        log.info("Executing email send: {}", sendEmail);
        //TODO implement the service call to the JavaMailSender
        RecipientModel recipient = sendEmail.getRecipients();
        assertNotEmpty(recipient.getTo(), "Recipient email address is required");
        assertNotNull(processedEmailBody.getSubject(), "Email subject is required");
        assertNotNull(processedEmailBody.getHtmlProcessedBody(), "Email HTML content is required");
        assertNotNull(processedEmailBody.getTextProcessedBody(), "Email plaintext content is required");
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, "UTF-8");
        try {
            messageHelper.setFrom(this.smtpConfiguration.getFromEmail());
            messageHelper.setTo(recipient.getTo().toArray(String[]::new));
            messageHelper.setSubject(processedEmailBody.getSubject());
            messageHelper.setText(processedEmailBody.getTextProcessedBody(), processedEmailBody.getHtmlProcessedBody());
            this.javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
