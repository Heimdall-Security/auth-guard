package com.heimdallauth.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

@Service
@Slf4j
public class TemplateRenderService {
    private final TemplateEngine textTemplateEngine;
    private final TemplateEngine htmlTemplateEngine;

    public TemplateRenderService() {
        this.textTemplateEngine = buildTextEngine();
        this.htmlTemplateEngine = buildHtmlEngine();
    }

    private static ITemplateResolver getTextStringTemplateResolver(){
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(org.thymeleaf.templatemode.TemplateMode.TEXT);
        return resolver;
    }
    private static ITemplateResolver getHtmlStringTemplateResolver(){
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(org.thymeleaf.templatemode.TemplateMode.HTML);
        return resolver;
    }
    private static TemplateEngine buildEngine(ITemplateResolver resolver){
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }
    private static TemplateEngine buildTextEngine(){
        return buildEngine(getTextStringTemplateResolver());
    }
    private static TemplateEngine buildHtmlEngine(){
        return buildEngine(getHtmlStringTemplateResolver());
    }
    public String render(String templatedString, Map<String, Object> variableValueMap, TemplateMode mode){
        switch (mode){
            case HTML:
                return renderHtmlString(templatedString, variableValueMap);
            case TEXT:
                return renderTemplatedString(templatedString, variableValueMap);
            default:
                throw new IllegalArgumentException("Invalid template mode");
        }
    }

    private String renderTemplatedString(String templatedString, Map<String, Object> variableValueMap){
        log.debug("Rendering templated string: {}", templatedString);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variableValueMap);
        return this.textTemplateEngine.process(templatedString, thymeleafContext);
    }
    private String renderHtmlString(String templatedString, Map<String, Object> variableValueMap){
        log.debug("Rendering templated string: {}", templatedString);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variableValueMap);
        return this.htmlTemplateEngine.process(templatedString, thymeleafContext);
    }
    public enum TemplateMode{
        HTML,
        TEXT
    }
}
