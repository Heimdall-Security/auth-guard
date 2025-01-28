package com.heimdallauth.server.commons.models.kratos;

import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistoryEntryModel {
    private String id;
    private Instant historyEntryTimestamp;
    private String className;
    private String entityId;
    private String changeType;
    private String transactionId;
}
