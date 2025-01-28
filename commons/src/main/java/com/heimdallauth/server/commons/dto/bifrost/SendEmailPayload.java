package com.heimdallauth.server.commons.dto.bifrost;

import com.heimdallauth.server.commons.models.bifrost.ConfigurationSet;
import com.heimdallauth.server.commons.models.bifrost.RecipientModel;
import com.heimdallauth.server.commons.models.bifrost.TemplateModel;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendEmailPayload {
    private RecipientModel recipients;
    private TemplateModel template;
    private ConfigurationSet activeConfigurationSet;
    private Object data;
    private String replyToEmailAddress;
}
