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
import com.mockio.user_service.dto.request.ProfileSyncRequest;
import com.mockio.user_service.dto.request.UserProfileUpdateRequest;
import com.mockio.user_service.dto.response.UserIdResponse;
import com.mockio.user_service.dto.response.UserProfileDetailResponse;
import com.mockio.user_service.dto.response.UserProfileResponse;
import com.mockio.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/internal/me/sync")
    public UserProfileResponse syncMyProfile(@RequestBody ProfileSyncRequest profileSyncRequest) {
        return  userProfileService.loadOrCreateFromToken(profileSyncRequest);
    }

    @GetMapping("/internal/by-keycloak-id/{keycloakUserId}")
    public UserIdResponse getUserId(@PathVariable String keycloakUserId) {
        return userProfileService.getUserId(keycloakUserId);
    }

    /**
     * 유저 프로필 변경
     * @param user
     * @param username
     * @param profileImage
     * @return
     */
    @PatchMapping(value = "/mypage/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Void>> updateProfile(
            @CurrentUser UserProfile user,
            @RequestPart("nickname") String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        userProfileService.updateMyProfile(user.getId(), nickname, profileImage);
        return Response.update(messageUtil.getMessage("response.update"));
    }


    /**
     * 유저 탈퇴
     * @param user
     * @return
     */
    @PatchMapping("/me/delete-profile")
    public ResponseEntity<Response<Void>> deleteProfile(@CurrentUser UserProfile user) {
        userProfileService.deleteProfile(user);
        return Response.update(messageUtil.getMessage("response.update"));
    }


    /**
     * 유저 세팅 정보 조회
     * @param user
     * @return
     */
    @GetMapping("/mypage/setting")
    public ResponseEntity<Response<UserProfileDetailResponse>> getUserProfileDetail(@CurrentUser UserProfile user) {
        return Response.ok(messageUtil.getMessage("response.read"), userProfileService.getUserProfileDetail(user.getId()));
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
