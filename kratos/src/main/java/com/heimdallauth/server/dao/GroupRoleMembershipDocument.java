package com.heimdallauth.server.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class GroupRoleMembershipDocument {
    @Id
    @Field("membershipId")
    private String membershipId;
    @Indexed
    private String groupId;
    private String roleId;
    private Instant createdOn;
    private Instant lastUpdatedOn;
}
