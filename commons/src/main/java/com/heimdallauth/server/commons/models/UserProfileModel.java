package com.heimdallauth.server.commons.models;

import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserProfileModel {
    private String id;
    private String username;
    private String emailAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImageUrl;
    private Instant createdOn;
    private Instant lastUpdatedOn;
    private Instant lastActivityTimestamp;
}
