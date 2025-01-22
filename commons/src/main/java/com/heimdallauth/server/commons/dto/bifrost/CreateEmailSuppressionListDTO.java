package com.heimdallauth.server.commons.dto.bifrost;

import com.heimdallauth.server.commons.models.bifrost.SuppressionListEmailEntry;

import java.util.List;

public class CreateEmailSuppressionListDTO {
    private String suppressionListName;
    private List<SuppressionListEmailEntry> suppressedEmails;
}
