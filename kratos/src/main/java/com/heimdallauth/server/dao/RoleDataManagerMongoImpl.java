package com.heimdallauth.server.dao;

import com.heimdallauth.server.commons.models.RoleModel;
import com.heimdallauth.server.datamanagers.RoleDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
@Slf4j
public class RoleDataManagerMongoImpl implements RoleDataManager {
    private static final String ROLES_COLLECTION = "role-collection";
    private final MongoTemplate mongoTemplate;

    public RoleDataManagerMongoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    private static RoleDocument convertToRoleDocument(RoleModel roleModel){
        return RoleDocument.builder()
                .id(roleModel.getId())
                .roleName(roleModel.getRoleName())
                .roleDescription(roleModel.getRoleDescription())
                .createdOn(roleModel.getCreatedOn())
                .lastUpdatedOn(roleModel.getLastUpdatedOn())
                .build();
    }
    private static RoleModel convertToRoleModel(RoleDocument roleDocument){
        return RoleModel.builder()
                .id(roleDocument.getId())
                .roleName(roleDocument.getRoleName())
                .roleDescription(roleDocument.getRoleDescription())
                .createdOn(roleDocument.getCreatedOn())
                .lastUpdatedOn(roleDocument.getLastUpdatedOn())
                .build();
    }

    @Override
    public RoleModel createRole(String roleName, String roleDescription) {
        RoleDocument roleDocumentToSave = RoleDocument.builder()
                .roleName(roleName)
                .roleDescription(roleDescription)
                .createdOn(Instant.now())
                .lastUpdatedOn(Instant.now())
                .build();
        RoleDocument savedRoleDocument = mongoTemplate.save(roleDocumentToSave, ROLES_COLLECTION);
        return convertToRoleModel(savedRoleDocument);
    }

    @Override
    public Optional<RoleModel> getRoleById(String roleId) {
        Query selectRoleFromId = Query.query(Criteria.where("id").is(roleId));
        return Optional.ofNullable(mongoTemplate.findOne(selectRoleFromId, RoleDocument.class, ROLES_COLLECTION)).map(RoleDataManagerMongoImpl::convertToRoleModel);
    }

    @Override
    public Optional<RoleModel> searchRoleUsingRoleNameOrRoleDescription(String searchTerm) {
        Query searchQuery = Query.query(Criteria.where("roleName").regex(searchTerm, "i").orOperator(Criteria.where("roleDescription").regex(searchTerm,"i")));
        return Optional.ofNullable(mongoTemplate.findOne(searchQuery, RoleDocument.class, ROLES_COLLECTION)).map(RoleDataManagerMongoImpl::convertToRoleModel);
    }

    @Override
    public Optional<RoleModel> updateRole(String roleId, String roleName, String roleDescription) {
        return Optional.empty();
    }
}
