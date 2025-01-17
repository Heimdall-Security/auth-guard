package com.heimdallauth.server.dao;

import com.heimdallauth.server.commons.constants.UserLifecycleStage;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfileDocument {
    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String username;
    @Indexed
    private String emailAddress;
    private UserLifecycleStage lifecycleStage;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Instant createdOn;
    private Instant lastUpdatedTimestamp;
}
