package com.heimdallauth.server.services;

import com.heimdallauth.server.commons.constants.UserLifecycleStage;
import com.heimdallauth.server.commons.models.UserProfileModel;
import com.heimdallauth.server.datamanagers.UserProfileDataManager;
import com.heimdallauth.server.exceptions.UserProfileNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserProfileService {
    private final UserProfileDataManager userProfileDataManager;

    public UserProfileService(UserProfileDataManager userProfileDataManager) {
        this.userProfileDataManager = userProfileDataManager;
    }

    public UserProfileModel provisionUserProfile(UserLifecycleStage lifecycleStage, UserProfileModel userProfileModel) {
        log.info("Provisioning user profile for user: Username: {}, Email:{}", userProfileModel.getUsername(),userProfileModel.getEmailAddress());
        UserProfileModel createdUserProfile =  userProfileDataManager.createNewUserProfile(
                userProfileModel.getUsername(),
                userProfileModel.getEmailAddress(),
                userProfileModel.getFirstName(),
                userProfileModel.getLastName(),
                userProfileModel.getPhoneNumber()
        );
        log.info("User profile provisioned successfully for user: id: {},Username: {}, Email:{}",createdUserProfile.getId(), userProfileModel.getUsername(),userProfileModel.getEmailAddress());
        if(lifecycleStage != UserLifecycleStage.PROVISIONED) {
            updateLifecycleStage(createdUserProfile.getId(), lifecycleStage);
        }
        return createdUserProfile;
    }
    public void updateLifecycleStage(String profileId, UserLifecycleStage lifecycleStage) {
        UserProfileModel currentUserProfile = this.userProfileDataManager.getUserProfileById(profileId).orElseThrow(() -> new UserProfileNotFound("User profile not found"));
        currentUserProfile.setLifecycleStage(lifecycleStage);
        this.userProfileDataManager.updateUserProfile(profileId, currentUserProfile);
    }
}
