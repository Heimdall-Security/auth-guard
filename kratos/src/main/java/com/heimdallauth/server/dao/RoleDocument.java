package com.heimdallauth.server.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

import static com.heimdallauth.server.constants.MongoCollectionConstants.ROLES_COLLECTION;

@Document(collection = ROLES_COLLECTION)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDocument {
    @Id
    private String id;
    private String roleName;
    private String roleDescription;
    private Instant createdOn;
    private Instant lastUpdatedOn;
}
