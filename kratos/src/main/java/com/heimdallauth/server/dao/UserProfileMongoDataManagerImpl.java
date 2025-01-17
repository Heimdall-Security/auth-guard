package com.heimdallauth.server.dao;

import com.heimdallauth.server.commons.constants.UserLifecycleStage;
import com.heimdallauth.server.commons.models.UserProfileModel;
import com.heimdallauth.server.datamanagers.UserProfileDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
@Slf4j
public class UserProfileMongoDataManagerImpl implements UserProfileDataManager {
    private final MongoTemplate mongoTemplate;
    private static final String USER_PROFILE_COLLECTION_NAME = "user_profile_collection";

    public UserProfileMongoDataManagerImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private static UserProfileModel convertToUserProfileModel(UserProfileDocument userProfileDocument) {
        return UserProfileModel.builder()
                .id(userProfileDocument.getId())
                .username(userProfileDocument.getUsername())
                .emailAddress(userProfileDocument.getEmailAddress())
                .lifecycleStage(userProfileDocument.getLifecycleStage())
                .firstName(userProfileDocument.getFirstName())
                .lastName(userProfileDocument.getLastName())
                .phoneNumber(userProfileDocument.getPhoneNumber())
                .createdOn(userProfileDocument.getCreatedOn())
                .lastUpdatedOn(userProfileDocument.getLastUpdatedTimestamp())
                .build();
    }
    private static UserProfileDocument convertToUserProfileDocument(UserProfileModel userProfileModel){
        return UserProfileDocument.builder()
                .id(userProfileModel.getId())
                .username(userProfileModel.getUsername())
                .emailAddress(userProfileModel.getEmailAddress())
                .lifecycleStage(userProfileModel.getLifecycleStage())
                .firstName(userProfileModel.getFirstName())
                .lastName(userProfileModel.getLastName())
                .phoneNumber(userProfileModel.getPhoneNumber())
                .createdOn(userProfileModel.getCreatedOn())
                .lastUpdatedTimestamp(userProfileModel.getLastUpdatedOn())
                .build();
    }
    @Override
    public UserProfileModel createNewUserProfile(String username, String emailAddress, String firstName, String lastName, String phoneNumber) {
        UserProfileDocument profileDocumentToSave = UserProfileDocument.builder()
                .createdOn(Instant.now())
                .lastUpdatedTimestamp(Instant.now())
                .emailAddress(emailAddress)
                .username(username)
                .phoneNumber(phoneNumber)
                .lifecycleStage(UserLifecycleStage.PROVISIONED)
                .build();
        profileDocumentToSave = this.mongoTemplate.save(profileDocumentToSave, USER_PROFILE_COLLECTION_NAME);
        return convertToUserProfileModel(profileDocumentToSave);
    }

    @Override
    public Optional<UserProfileModel> getUserProfileById(String profileId) {
        Query selectUserProfileById = Query.query(Criteria.where("id").is(profileId));
        return Optional.ofNullable(this.mongoTemplate.findOne(selectUserProfileById, UserProfileDocument.class, USER_PROFILE_COLLECTION_NAME)).map(UserProfileMongoDataManagerImpl::convertToUserProfileModel);
    }

    @Override
    public Optional<UserProfileModel> searchUserProfile(String searchTerm) {
        Query searchQuery = new Query();
        searchQuery.addCriteria(Criteria.where("username").is(searchTerm).orOperator(Criteria.where("emailAddress").is(searchTerm)));
        return Optional.ofNullable(this.mongoTemplate.findOne(searchQuery, UserProfileDocument.class, USER_PROFILE_COLLECTION_NAME)).map(UserProfileMongoDataManagerImpl::convertToUserProfileModel);
    }

    @Override
    public Optional<UserProfileModel> updateUserProfile(String profileId, UserProfileModel updatedUserProfileModel) {
        return Optional.empty();
    }
}
