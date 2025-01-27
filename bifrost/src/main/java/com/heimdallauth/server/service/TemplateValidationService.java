package com.heimdallauth.server.service;

import com.heimdallauth.server.commons.models.TemplateModel;
import com.heimdallauth.server.datamanagers.TemplateDataManager;
import com.heimdallauth.server.exceptions.TemplateAlreadyExists;
import com.heimdallauth.server.exceptions.TemplateDoesNotExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TemplateValidationService {
    private final TemplateDataManager templateDataManager;

    public TemplateValidationService(TemplateDataManager templateDataManager) {
        this.templateDataManager = templateDataManager;
    }
    public void validateTemplate(TemplateModel payloadToValidate){
        validateUniqueTemplateName(payloadToValidate.getTemplateName());
    }
    public void validateUniqueTemplateName(String templateName){
        try{
            this.templateDataManager.getTemplateByName(templateName);
        }catch(TemplateDoesNotExists e){
            return;
        }
        throw new TemplateAlreadyExists("Template with name " + templateName + " already exists");
    }
}
