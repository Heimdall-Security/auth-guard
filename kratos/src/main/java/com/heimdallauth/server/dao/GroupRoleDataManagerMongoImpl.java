package com.heimdallauth.server.dao;

import com.heimdallauth.server.commons.models.GroupModel;
import com.heimdallauth.server.commons.models.RoleModel;
import com.heimdallauth.server.datamanagers.GroupDataManager;
import com.heimdallauth.server.datamanagers.RoleDataManager;
import com.heimdallauth.server.exceptions.GroupNotFound;
import com.heimdallauth.server.models.GroupAggregationModel;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupRoleDataManagerMongoImpl implements GroupDataManager, RoleDataManager {
    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION_GROUPS = "groups-collection";
    private static final String COLLECTION_GROUP_ROLE_MEMBERSHIPS = "group-role-memberships-collection";
    private static final String ROLES_COLLECTION = "role-collection";

    public GroupRoleDataManagerMongoImpl(MongoTemplate mongoTemplate) {
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

    public static GroupModel convertGroupDocumentToGroupModel(GroupDocument groupDocument) {
        return GroupModel.builder()
                .id(StringUtils.hasLength(groupDocument.getId()) ? groupDocument.getId() : null)
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
        documentToSave.setId(UUID.randomUUID().toString());
        GroupDocument savedDocument = this.mongoTemplate.save(documentToSave, COLLECTION_GROUPS);
        return this.getGroup(savedDocument.getId()).orElseThrow(() -> new GroupNotFound("The group not found", savedDocument.getId()));
    }

    @Override
    public List<GroupModel> createGroup(List<GroupModel> groupModels) {
        List<GroupDocument> documentsToSave = groupModels.stream()
                .map(GroupRoleDataManagerMongoImpl::convertGroupModelToGroupDocument)
                .toList();
        List<GroupDocument> savedDocuments = (List<GroupDocument>) this.mongoTemplate.insertAll(documentsToSave);
        return savedDocuments.stream()
                .map(GroupRoleDataManagerMongoImpl::convertGroupDocumentToGroupModel)
                .toList();
    }

    @Override
    public GroupModel updateGroup(String groupId, GroupModel updatedGroupModel) {
        return null;
    }

    @Override
    public Optional<GroupModel> getGroup(String groupId) {
        Aggregation mongoGroupSelectionAggregationPipeline = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(groupId)),
                Aggregation.lookup(COLLECTION_GROUP_ROLE_MEMBERSHIPS, "_id", "groupId", "rolesId"),
                Aggregation.lookup(ROLES_COLLECTION, "rolesId.roleId", "_id", "roles")
        );
        GroupAggregationModel aggregationResults = this.mongoTemplate.aggregate(mongoGroupSelectionAggregationPipeline, COLLECTION_GROUPS, GroupAggregationModel.class).getUniqueMappedResult();
        return Optional.ofNullable(aggregationResults).map(GroupAggregationModel::toGroupModel);
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
                .map(GroupRoleDataManagerMongoImpl::convertGroupDocumentToGroupModel);
    }

    @Override
    public List<GroupModel> getAllGroups() {
        Aggregation mongoGroupSelectionAggregationPipeline = Aggregation.newAggregation(
                Aggregation.lookup(COLLECTION_GROUP_ROLE_MEMBERSHIPS, "_id", "groupId", "rolesId"),
                Aggregation.lookup(ROLES_COLLECTION, "rolesId.roleId", "_id", "roles")
        );
        List<GroupAggregationModel> aggregationResults = this.mongoTemplate.aggregate(mongoGroupSelectionAggregationPipeline, COLLECTION_GROUPS, GroupAggregationModel.class).getMappedResults();
        return aggregationResults.stream().map(GroupAggregationModel::toGroupModel).toList();
    }

    @Override
    public GroupModel updateGroupRoleMapping(String groupId, List<String> roleIds) {
        Query selectGroupQuery = Query.query(Criteria.where("id").is(groupId));
        Query selectionRoleMembershipQuery = Query.query(Criteria.where("groupId").is(groupId));
        GroupDocument groupDocument = Optional.ofNullable(this.mongoTemplate.findOne(selectGroupQuery, GroupDocument.class, COLLECTION_GROUPS)).orElseThrow(() -> new GroupNotFound("The group not found", groupId));
        List<GroupRoleMembershipDocument> groupMemberships = this.mongoTemplate.find(selectionRoleMembershipQuery, GroupRoleMembershipDocument.class, COLLECTION_GROUP_ROLE_MEMBERSHIPS);
        List<String> filteredRoleMemberships = roleIds.stream().filter(roleId -> groupMemberships.stream().noneMatch(groupRoleMembershipDocument -> groupRoleMembershipDocument.getRoleId().equals(roleId))).toList();
        List<GroupRoleMembershipDocument> newRoleMemberships = filteredRoleMemberships.stream()
                .map(roleId -> GroupRoleMembershipDocument.builder()
                        .membershipId(UUID.randomUUID().toString())
                        .groupId(groupId)
                        .roleId(roleId)
                        .createdOn(Instant.now())
                        .lastUpdatedOn(Instant.now())
                        .build())
                .toList();
        this.mongoTemplate.insert(newRoleMemberships, COLLECTION_GROUP_ROLE_MEMBERSHIPS);
        return this.getGroup(groupId).orElseThrow(() -> new GroupNotFound("The group not found", groupId));
    }

    @Override
    public RoleModel createRole(String roleName, String roleDescription) {
        RoleDocument roleDocumentToSave = RoleDocument.builder()
                .id(UUID.randomUUID().toString())
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
        return Optional.ofNullable(mongoTemplate.findOne(selectRoleFromId, RoleDocument.class, ROLES_COLLECTION)).map(GroupRoleDataManagerMongoImpl::convertToRoleModel);
    }

    @Override
    public List<RoleModel> getRolesByIds(List<String> ids) {
        Query selectRolesByIds = Query.query(Criteria.where("id").in(ids));
        return mongoTemplate.find(selectRolesByIds, RoleDocument.class, ROLES_COLLECTION).stream().map(GroupRoleDataManagerMongoImpl::convertToRoleModel).toList();
    }

    @Override
    public Optional<RoleModel> searchRoleUsingRoleNameOrRoleDescription(String searchTerm) {
        Query searchQuery = Query.query(Criteria.where("roleName").regex(searchTerm, "i").orOperator(Criteria.where("roleDescription").regex(searchTerm,"i")));
        return Optional.ofNullable(mongoTemplate.findOne(searchQuery, RoleDocument.class, ROLES_COLLECTION)).map(GroupRoleDataManagerMongoImpl::convertToRoleModel);
    }

    @Override
    public Optional<RoleModel> updateRole(String roleId, String roleName, String roleDescription) {
        return Optional.empty();
    }

    @Override
    public List<RoleModel> getAllRoles() {
        return this.mongoTemplate.findAll(RoleDocument.class, ROLES_COLLECTION).stream().map(GroupRoleDataManagerMongoImpl::convertToRoleModel).toList();
    }

    @Override
    public void deleteRoleById(String roleId) {
        Query groupMembershipDeleteQuery = Query.query(Criteria.where("roleId").is(roleId));
        Query roleIdDeleteQuery = Query.query(Criteria.where("id").is(roleId));
        this.mongoTemplate.remove(groupMembershipDeleteQuery, GroupRoleMembershipDocument.class);
        this.mongoTemplate.remove(roleIdDeleteQuery, RoleDocument.class);
    }
}
