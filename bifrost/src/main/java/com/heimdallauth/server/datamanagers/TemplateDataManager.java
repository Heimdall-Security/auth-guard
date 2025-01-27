package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.TemplateModel;

import java.util.List;

public interface TemplateDataManager {
    TemplateModel createTemplate(String templateName, String templateHtmlContent, String templatePlaintextContent, String templateSubject);
    TemplateModel getTemplateById(String id);
    TemplateModel getTemplateByName(String templateName);
    TemplateModel updateTemplate(String id, String templateName, String templateHtmlContent, String templatePlaintextContent, String templateSubject);
    void deleteTemplate(String id);
    List<TemplateModel> getAllTemplates();
}
