package com.heimdallauth.server.datamanagers;

import com.heimdallauth.server.commons.models.UserProfileModel;

import java.util.List;
import java.util.Optional;

public interface UserProfileDataManager {
    UserProfileModel createNewUserProfile(String username, String emailAddress, String firstName, String lastName, String phoneNumber);

    List<UserProfileModel> getAllUserProfiles();

    Optional<UserProfileModel> getUserProfileById(String profileId);

    List<UserProfileModel> searchUserProfile(String searchTerm);

    Optional<UserProfileModel> searchUserProfileWithUsernameOrEmailAddress(String username, String emailAddress);

    Optional<UserProfileModel> updateUserProfile(String profileId, UserProfileModel updatedUserProfileModel);
}
