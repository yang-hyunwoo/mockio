package com.mockio.user_service.controller;

/**
 * UserProfileController.
 *
 * 유저 관련 API를 제공합니다.
 */

import com.mockio.common_security.annotation.CurrentUser;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.request.UserProfileUpdateRequest;
import com.mockio.user_service.dto.response.UserProfileResponse;
import com.mockio.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final MessageUtil messageUtil;

    /**
     * keycloak 로그인 시 userProfile 저장
     * @param jwt
     * @return
     */
    @PostMapping("/me/sync")
    public ResponseEntity<Response<UserProfileResponse>> syncMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return Response.ok(messageUtil.getMessage("response.read"),
                userProfileService.loadOrCreateFromToken(jwt));
    }

    /**
     * 유저 프로필 변경
     *
     * @param user
     * @param userProfileUpdateRequest
     * @return
     */
    @PatchMapping("/me/update-profile")
    public ResponseEntity<Response<Void>> updateMyProfile(@CurrentUser UserProfile user,
                                                          UserProfileUpdateRequest userProfileUpdateRequest) {
        userProfileService.updateMyProfile(user, userProfileUpdateRequest);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    @GetMapping("/public/me/public-page")
    public ResponseEntity<?> publicPage(@CurrentUser(required = false) UserProfile user) {
        if (user == null) {
            // 로그인 안한 사용자
        } else {
            // 로그인 사용자
        }
        return ResponseEntity.ok().build();
    }

}
