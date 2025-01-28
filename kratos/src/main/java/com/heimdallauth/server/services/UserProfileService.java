package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.constants.UserLifecycleStage;
import com.heimdallauth.server.commons.models.kratos.UserProfileModel;
import com.heimdallauth.server.datamanagers.UserProfileDataManager;
import com.heimdallauth.server.exceptions.UserProfileAlreadyExist;
import com.heimdallauth.server.exceptions.UserProfileNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserProfileService {
    private final UserProfileDataManager userProfileDataManager;

    public UserProfileService(UserProfileDataManager userProfileDataManager) {
        this.userProfileDataManager = userProfileDataManager;
    }

    public UserProfileModel provisionUserProfile(UserProfileModel userProfileModel) {
        return provisionUserProfile(UserLifecycleStage.PROVISIONED, userProfileModel);
    }

    public UserProfileModel provisionUserProfile(UserLifecycleStage lifecycleStage, UserProfileModel userProfileModel) {
        boolean proceed = this.userProfileDataManager.searchUserProfileWithUsernameOrEmailAddress(userProfileModel.getUsername(), userProfileModel.getEmailAddress()).isEmpty();
        if (proceed) {
            log.info("Provisioning user profile for user: Username: {}, Email:{}", userProfileModel.getUsername(), userProfileModel.getEmailAddress());
            UserProfileModel createdUserProfile = userProfileDataManager.createNewUserProfile(
                    userProfileModel.getUsername(),
                    userProfileModel.getEmailAddress(),
                    userProfileModel.getFirstName(),
                    userProfileModel.getLastName(),
                    userProfileModel.getPhoneNumber()
            );
            log.info("User profile provisioned successfully for user: id: {},Username: {}, Email:{}", createdUserProfile.getId(), userProfileModel.getUsername(), userProfileModel.getEmailAddress());
            if (lifecycleStage != UserLifecycleStage.PROVISIONED) {
                updateLifecycleStage(createdUserProfile.getId(), lifecycleStage);
            }
            return createdUserProfile;
        } else {
            throw new UserProfileAlreadyExist("The user profile with username or email already exist");
        }
    }

    public List<UserProfileModel> searchUserProfile(String searchTerm) {
        return this.userProfileDataManager.searchUserProfile(searchTerm);
    }

    public void updateLifecycleStage(String profileId, UserLifecycleStage lifecycleStage) {
        UserProfileModel currentUserProfile = this.userProfileDataManager.getUserProfileById(profileId).orElseThrow(() -> new UserProfileNotFound("User profile not found"));
        currentUserProfile.setLifecycleStage(lifecycleStage);
        this.userProfileDataManager.updateUserProfile(profileId, currentUserProfile);
    }

    public List<UserProfileModel> getAllUserProfiles() {
        return this.userProfileDataManager.getAllUserProfiles();
    }
}
