package com.heimdallauth.server.dao;

import com.heimdallauth.server.datamanagers.GroupDataManager;
import com.heimdallauth.server.commons.models.GroupModel;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
public class GroupsMongoDataManager implements GroupDataManager {
    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION_GROUPS = "groups-collection";

    public GroupsMongoDataManager(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public static GroupModel convertGroupDocumentToGroupModel(GroupDocument groupDocument) {
        return GroupModel.builder()
                .groupName(groupDocument.getGroupName())
                .groupDescription(groupDocument.getGroupDescription())
                .tenantId(groupDocument.getTenantId())
                .roles(List.of())
                .build();
    }
    public static GroupDocument convertGroupModelToGroupDocument(GroupModel groupModel) {
        return GroupDocument.builder()
                .id(StringUtils.hasLength(groupModel.getId()) ? groupModel.getId() : null )
                .groupName(groupModel.getGroupName())
                .groupDescription(groupModel.getGroupDescription())
                .tenantId(groupModel.getTenantId())
                .build();
    }

    @Override
    public GroupModel createGroup(GroupModel groupModel) {
        GroupDocument documentToSave = convertGroupModelToGroupDocument(groupModel);
        GroupDocument savedDocument = this.mongoTemplate.save(documentToSave, COLLECTION_GROUPS);
        return convertGroupDocumentToGroupModel(savedDocument);
    }

    @Override
    public List<GroupModel> createGroup(List<GroupModel> groupModels) {
        List<GroupDocument> documentsToSave = groupModels.stream()
                .map(GroupsMongoDataManager::convertGroupModelToGroupDocument)
                .toList();
        List<GroupDocument> savedDocuments = (List<GroupDocument>) this.mongoTemplate.insertAll(documentsToSave);
        return savedDocuments.stream()
                .map(GroupsMongoDataManager::convertGroupDocumentToGroupModel)
                .toList();
    }

    @Override
    public GroupModel updateGroup(String groupId, GroupModel updatedGroupModel) {
        return null;
    }

    @Override
    public GroupModel getGroup(String groupId) {
        return null;
    }

    @Override
    public List<GroupModel> getGroups(List<String> groupIds) {
        return List.of();
    }

    @Override
    public List<GroupModel> getGroupsByTenantId(String tenantId) {
        return List.of();
    }

    @Override
    public List<GroupModel> getGroupsByTenantIdAndGroupName(String tenantId, String groupName) {
        return List.of();
    }

    @Override
    public void addUserToGroupMembership(String groupId, String memberId) {

    }

    @Override
    public void addUsersToGroupMembership(String groupId, List<String> memberIds) {

    }

    @Override
    public void removeUserFromGroupMembership(String groupId, String memberId) {

    }

    @Override
    public void removeUsersFromGroupMembership(String groupId, List<String> memberIds) {

    }

    @Override
    public List<String> getGroupMembers(String groupId) {
        return List.of();
    }

    @Override
    public Optional<GroupModel> searchGroup(String searchTerm) {
        Query searchDBQuery = new Query();
        searchDBQuery.addCriteria(new Criteria().orOperator(
                Criteria.where("groupName").regex(searchTerm, "i"),
                Criteria.where("groupDescription").regex(searchTerm, "i")
        ));
        return Optional.ofNullable(this.mongoTemplate.findOne(searchDBQuery, GroupDocument.class, COLLECTION_GROUPS))
                .map(GroupsMongoDataManager::convertGroupDocumentToGroupModel);
    }

    @Override
    public List<GroupModel> getAllGroups() {
        List<GroupDocument> allGroups = this.mongoTemplate.findAll(GroupDocument.class, COLLECTION_GROUPS);
        return allGroups.stream()
                .map(GroupsMongoDataManager::convertGroupDocumentToGroupModel)
                .toList();
    }
}
