package com.mockio.core_service.user.controller;

/**
 * UserProfileController.
 *
 * 유저 관련 API를 제공합니다.
 */

import com.mockio.common_security.annotation.CurrentUser;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.dto.response.UserProfileDetailResponse;
import com.mockio.core_service.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "사용자 정보"
)
@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final MessageUtil messageUtil;

    /**
     * 유저 프로필 변경
     *
     * @param user
     * @param profileImage
     * @return
     */
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "프로필 변경")
    @PatchMapping(value = "/mypage/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Void>> updateProfile(
            @CurrentUser @Parameter(description = "사용자", example = "user") User user,
            @RequestPart("nickname") @Parameter(description = "닉네임", example = "sdfas") String nickname,
            @RequestPart(value = "profileImage", required = false) @Parameter(description = "파일", example = "file") MultipartFile profileImage
    ) {
        userProfileService.updateMyProfile(user.getId(), nickname, profileImage);
        return Response.update(messageUtil.getMessage("response.update"));
    }

    /**
     * 유저 세팅 정보 조회
     * @param user
     * @return
     */
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "프로필 / 면접 세팅 조회")
    @GetMapping("/mypage/setting")
    public ResponseEntity<Response<UserProfileDetailResponse>> getUserProfileDetail(@CurrentUser @Parameter(description = "사용자", example = "user") User user) {
        return Response.ok(messageUtil.getMessage("response.read"), userProfileService.getUserProfileDetail(user.getId()));
    }

}
