package com.heimdallauth.server.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class ThymeleafTemplateResolverConfiguration {
    @Bean
    @Qualifier("stringHtmlTemplateResolver")
    public TemplateEngine stringHtmlTemplateResolver(){
        TemplateEngine engine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        engine.setTemplateResolver(resolver);
        return engine;
    }
    @Bean
    @Qualifier("stringTextTemplateResolver")
    public TemplateEngine stringTextTemplateResolver(){
        TemplateEngine engine = new TemplateEngine();
        StringTemplateResolver resolver = new StringTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        engine.setTemplateResolver(resolver);

        return engine;
    }
}
