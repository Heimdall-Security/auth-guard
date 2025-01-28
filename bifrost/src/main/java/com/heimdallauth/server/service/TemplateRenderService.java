package com.heimdallauth.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@Slf4j
public class TemplateRenderService {
    private final SpringTemplateEngine springTemplateEngine;

    public TemplateRenderService(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }
    public String renderTemplatedString(String templatedString, Map<String, Object> variableValueMap){
        log.debug("Rendering templated string: {}", templatedString);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variableValueMap);
        return springTemplateEngine.process(templatedString, thymeleafContext);
    }
}
