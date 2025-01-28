package com.heimdallauth.server.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProcessedEmailBodyModel {
    private String subject;
    private String htmlProcessedBody;
    private String textProcessedBody;

}
