package com.heimdallauth.server.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import static com.heimdallauth.server.constants.MongoCollectionConstants.GROUPS_COLLECTION;


@Document(collection = GROUPS_COLLECTION)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GroupDocument {
    @Id
    private String id;
    private String groupName;
    private String groupDescription;
    @Indexed
    private String tenantId;
}
