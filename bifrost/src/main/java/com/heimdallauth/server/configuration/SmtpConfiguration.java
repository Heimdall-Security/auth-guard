package com.heimdallauth.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class SmtpConfiguration {
    @Bean
    public JavaMailSender javaMailSender(HeimdallSmtpConfiguration smtpConfiguration) throws Exception {
        try{
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(smtpConfiguration.getHost());
            mailSender.setPort(Integer.parseInt(smtpConfiguration.getPort()));
            mailSender.setUsername(smtpConfiguration.getLoginUsername());
            mailSender.setPassword(smtpConfiguration.getLoginPassword());
            return mailSender;
        }catch (Exception e){
            throw new Exception("Error creating JavaMailSender bean", e);
        }
    }
}
