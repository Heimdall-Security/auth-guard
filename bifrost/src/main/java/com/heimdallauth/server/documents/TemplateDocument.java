package com.heimdallauth.server.documents;

import com.heimdallauth.server.commons.models.bifrost.TemplateModel;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateDocument {
    @Id
    private String id;
    private String templateName;
    private String templateHtmlContent;
    private String templatePlaintextContent;
    private String templateSubject;
    private Instant createdAt;
    private Instant lastUpdatedAt;

    public static TemplateModel toTemplateModel(TemplateDocument document){
        return TemplateModel.builder()
                .id(document.getId())
                .templateName(document.getTemplateName())
                .templateHtmlContent(document.getTemplateHtmlContent())
                .templatePlaintextContent(document.getTemplatePlaintextContent())
                .templateSubject(document.getTemplateSubject())
                .createdAt(document.getCreatedAt())
                .lastUpdatedAt(document.getLastUpdatedAt())
                .build();
    }
}
