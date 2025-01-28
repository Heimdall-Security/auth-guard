package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.dto.bifrost.SendEmailPayload;
import com.heimdallauth.server.service.SendEmailProcessor;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/emails")
public class ExecuteSendEmail {
    private final SendEmailProcessor sendEmailProcessor;

    @Autowired
    public ExecuteSendEmail(SendEmailProcessor sendEmailProcessor) {
        this.sendEmailProcessor = sendEmailProcessor;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendEmail(@RequestBody SendEmailPayload sendEmailPayload) throws MessagingException {
        this.sendEmailProcessor.processSendEmailApiPayload(sendEmailPayload);
        return ResponseEntity.ok().build();
    }
}
