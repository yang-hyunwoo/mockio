package com.mockio.user_service.controller;

/**
 * UserProfileController.
 *
 * 유저 정보 조회 등 유저 관련 API를 제공합니다.
 */

import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.user_service.Mapper.UserProfileMapper;
import com.mockio.user_service.dto.response.UserProfileResponse;
import com.mockio.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
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
    public ResponseEntity<Response<UserProfileResponse>> getMyProfile(@AuthenticationPrincipal Jwt jwt) {


        return Response.ok(messageUtil.getMessage("response.read"),
                UserProfileMapper.from(userProfileService.loadOrCreateFromToken(jwt)));
    }

}
