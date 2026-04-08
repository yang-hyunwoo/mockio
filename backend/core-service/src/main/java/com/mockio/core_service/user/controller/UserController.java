package com.mockio.core_service.user.controller;

import com.mockio.common_security.annotation.CurrentUser;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.dto.UserAuthInfoResponse;
import com.mockio.core_service.user.dto.request.*;
import com.mockio.core_service.user.dto.response.UserInfoResponse;
import com.mockio.core_service.user.service.PasswordResetTokenService;
import com.mockio.core_service.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 정보",
        description = """
               사용자 정보 관련 API입니다.
                
                - 비밀번호 변경
                - 비밀번호 찾기
                - 탈퇴
                - 프로필 수정
                - 프로필 조회
                - 면접 세팅 조회
                """
)
@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageUtil messageUtil;
    private final PasswordResetTokenService passwordResetTokenService;

    /**
     * 이메일로 사용자 정보 체크 (내부 호출)
     *
     * @param email
     * @return
     */
    @Hidden
    @GetMapping("/internal/login/{email}")
    public UserAuthInfoResponse getUserAuthInfo(@PathVariable @Parameter(description = "이메일" ,example = "test@naver.com") String email ) {
        return userService.getUserAuthInfo(email);
    }

    /**
     * 사용자 정보 조회 (내부 호출)
     *
     * @param userId
     * @return
     */
    @Hidden
    @GetMapping("/internal/user-info/{userId}")
    public UserInfoResponse userDetail(@PathVariable @Parameter(description = "사용자 ID" , example = "1") Long userId) {
        return userService.userDetail(userId);
    }

    /**
     * 로그인 성공 (내부 호출)
     *
     * @param loginSuccessRequest
     */
    @Hidden
    @PatchMapping("/internal/login-success")
    public void resetFailCount(@RequestBody @Valid LoginSuccessRequest loginSuccessRequest) {
        userService.resetFailCount(loginSuccessRequest.userId());
    }

    /**
     * 로그인 실패 (내부 호출)
     *
     * @param loginFailureRequest
     */
    @Hidden
    @PatchMapping("/internal/login-failure")
    public void loginFailure(@RequestBody @Valid LoginFailureRequest loginFailureRequest) {
        userService.loginFailure(loginFailureRequest.email());
    }

    /**
     * 내부 호출 (oauth 로그인)
     *
     * @param oauthUserRequest
     * @return
     */
    @Hidden
    @PostMapping("/internal/oauth-login")
    public UserAuthInfoResponse oauthLogin(@RequestBody @Valid OauthUserRequest oauthUserRequest) {
        return userService.oauthLogin(oauthUserRequest);
    }

    /**
     * 비밀번호 찾기
     *
     * @param passwordFindRequest
     */
    @Operation(summary = "비밀번호 찾기")
    @PostMapping("/public/password-find")
    public void passwordFind(@RequestBody @Valid PasswordFindRequest passwordFindRequest) {
        userService.sendPasswordEmail(passwordFindRequest.email().trim());

    }

    /**
     * 비밀번호 찾기-> 토큰 검증
     *
     * @param token
     * @return
     */
    @Operation(summary = "비밀번호 찾기 토큰 검증")
    @GetMapping("/public/password/reset/validate")
    public ResponseEntity<Response<Void>> validateToken(@RequestParam @Parameter(description = "토큰" , example = "asdf") String token) {
        passwordResetTokenService.validateToken(token);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

    /**
     * 비밀번호 찾기 -> 비밀번호 변경
     *
     * @param passwordChangeRequest
     * @return
     */
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "비밀번호 변경")
    @PostMapping("/public/password/change")
    public ResponseEntity<Response<Void>> resetPasswordChange(@RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {
        userService.resetPasswordChange(passwordChangeRequest);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

    /**
     * 사용자 탈퇴
     *
     * @param user
     * @param request
     * @param response
     * @return
     */
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "사용자 탈퇴")
    @PatchMapping("/delete")
    public ResponseEntity<Response<Void>> getUserProfileDetail(
            @CurrentUser @Parameter(description = "사용자", example = "user") User user,
            @RequestBody @Valid MypagePasswordChangeRequest request,
            HttpServletResponse response
    ) {
        userService.deleteUser(user, request, response);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

    /**
     * 마이페이지 비밀번호 변경
     *
     * @param user
     * @param request
     * @return
     */
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "마이페이지 비밀번호 변경")
    @PostMapping("/password-change")
    public ResponseEntity<Response<Void>> updatePasswordChange(
            @CurrentUser @Parameter(description = "사용자", example = "user") User user,
            @RequestBody MypagePasswordChangeRequest request
    ) {
        userService.updatePasswordChange(user, request);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

}
