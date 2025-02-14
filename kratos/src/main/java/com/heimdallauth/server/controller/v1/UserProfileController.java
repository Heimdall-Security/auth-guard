package com.heimdallauth.server.controller.v1;

import com.heimdallauth.server.commons.models.kratos.UserProfileModel;
import com.heimdallauth.server.services.UserProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user-profile")
@Tag(name = "User Profile", description = "User Profile API")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/profiles/search")
    public ResponseEntity<List<UserProfileModel>> searchUserProfile(@RequestParam("searchTerm") String searchTerm) {
        return ResponseEntity.ok(this.userProfileService.searchUserProfile(searchTerm));
    }

    @PostMapping("/profiles/create")
    public ResponseEntity<UserProfileModel> createNewUserProfile(@RequestBody UserProfileModel userProfileModel) {
        return ResponseEntity.ok(this.userProfileService.provisionUserProfile(userProfileModel));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfileModel>> getAllUserProfiles() {
        return ResponseEntity.ok(this.userProfileService.getAllUserProfiles());
    }
}
