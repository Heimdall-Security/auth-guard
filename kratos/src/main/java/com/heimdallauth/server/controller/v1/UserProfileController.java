package com.heimdallauth.server.controller.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-profile")
@Tag(name = "User Profile", description = "User Profile API")
public class UserProfileController {
}
