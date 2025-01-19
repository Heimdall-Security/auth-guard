package com.heimdallauth.server.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.heimdallauth.server.constants.MongoCollectionConstants.GROUP_ROLE_MEMBERSHIP_COLLECTION;

@Document(collection = GROUP_ROLE_MEMBERSHIP_COLLECTION)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupMembershipDocument {
    @Id
    private String id;
    @Indexed
    private String groupId;
    @Indexed
    private String memberId;
}
