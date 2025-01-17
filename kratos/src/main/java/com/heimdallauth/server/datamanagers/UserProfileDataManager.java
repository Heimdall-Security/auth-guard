package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.UserProfileModel;

import java.util.Optional;

public interface UserProfileDataManager {
    UserProfileModel createNewUserProfile(String username, String emailAddress, String firstName, String lastName, String phoneNumber);
    Optional<UserProfileModel> getUserProfileById(String profileId);
    Optional<UserProfileModel> searchUserProfile(String searchTerm);
    Optional<UserProfileModel> updateUserProfile(String profileId, UserProfileModel updatedUserProfileModel);
}
