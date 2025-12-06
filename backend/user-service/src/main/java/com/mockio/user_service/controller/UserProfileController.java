package com.mockio.user_service.controller;

/**
 * UserProfileController.
 *
 * 유저 정보 조회 등 유저 관련 API를 제공합니다.
 */


import com.mockio.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public String getMyProfile(@AuthenticationPrincipal Jwt jwt) {
         userProfileService.loadOrCreateFromToken(jwt);

        return "aaa";
    }

}
