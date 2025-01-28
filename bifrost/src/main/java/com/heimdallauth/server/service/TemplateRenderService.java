package com.heimdallauth.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@Slf4j
public class TemplateRenderService {
    private final TemplateEngine htmlTemplateEngine;
    private final TemplateEngine textTemplateEngine;


    public TemplateRenderService(@Qualifier("stringHtmlTemplateResolver") TemplateEngine htmlTemplateEngine, @Qualifier("stringTextTemplateResolver") TemplateEngine textTemplateEngine) {
        this.htmlTemplateEngine = htmlTemplateEngine;
        this.textTemplateEngine = textTemplateEngine;
    }
    public String render(String templatedString, Map<String, Object> variableValueMap, TemplateMode templateMode){
            if(templateMode == TemplateMode.HTML){
                return renderHtmlString(templatedString, variableValueMap);
            } else if(templateMode == TemplateMode.TEXT) {
                return renderTemplatedString(templatedString, variableValueMap);
            }
        return null;
    }
    private String renderTemplatedString(String templatedString, Map<String, Object> variableValueMap){
        log.debug("Rendering templated string: {}", templatedString);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variableValueMap);
        return textTemplateEngine.process(templatedString, thymeleafContext);
    }
    private String renderHtmlString(String templatedString, Map<String, Object> variableValueMap){
        log.debug("Rendering templated string: {}", templatedString);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variableValueMap);
        return htmlTemplateEngine.process(templatedString, thymeleafContext);
    }

    public enum TemplateMode{
        HTML,
        TEXT
    }
}
