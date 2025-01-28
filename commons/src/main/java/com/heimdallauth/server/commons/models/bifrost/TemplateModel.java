package com.heimdallauth.server.commons.models.bifrost;

import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateModel {
    private String id;
    private String templateName;
    private String templateHtmlContent;
    private String templatePlaintextContent;
    private String templateSubject;
    private Instant createdAt;
    private Instant lastUpdatedAt;
}
