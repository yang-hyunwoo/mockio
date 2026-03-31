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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/internal/login/{email}")
    public UserAuthInfoResponse getUserAuthInfo(@PathVariable String email) {
        return userService.getUserAuthInfo(email);
    }

    /**
     * 사용자 정보 조회 (내부 호출)
     *
     * @param userId
     * @return
     */
    @GetMapping("/internal/user-info/{userId}")
    public UserInfoResponse userDetail(@PathVariable Long userId) {
        return userService.userDetail(userId);
    }

    /**
     * 로그인 성공 (내부 호출)
     *
     * @param loginSuccessRequest
     */
    @PatchMapping("/internal/login-success")
    public void resetFailCount(@RequestBody LoginSuccessRequest loginSuccessRequest) {
        userService.resetFailCount(loginSuccessRequest.userId());
    }

    /**
     * 로그인 실패 (내부 호출)
     *
     * @param loginFailureRequest
     */
    @PatchMapping("/internal/login-failure")
    public void loginFailure(@RequestBody LoginFailureRequest loginFailureRequest) {
        userService.loginFailure(loginFailureRequest.email());
    }

    /**
     * 내부 호출 (oauth 로그인)
     *
     * @param oauthUserRequest
     * @return
     */
    @PostMapping("/internal/oauth-login")
    public UserAuthInfoResponse oauthLogin(@RequestBody OauthUserRequest oauthUserRequest) {
        return userService.oauthLogin(oauthUserRequest);
    }

    /**
     * 비밀번호 찾기
     *
     * @param passwordFindRequest
     */
    @PostMapping("/public/password-find")
    public void passwordFind(@RequestBody PasswordFindRequest passwordFindRequest) {
        userService.sendPasswordEmail(passwordFindRequest.email().trim());

    }

    /**
     * 비밀번호 찾기-> 토큰 검증
     *
     * @param token
     * @return
     */
    @GetMapping("/public/password/reset/validate")
    public ResponseEntity<Response<Void>> validateToken(@RequestParam String token) {
        passwordResetTokenService.validateToken(token);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

    /**
     * 비밀번호 찾기 -> 비밀번호 변경
     *
     * @param passwordChangeRequest
     * @return
     */
    @PostMapping("/public/password/change")
    public ResponseEntity<Response<Void>> resetPasswordChange(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.resetPasswordChange(passwordChangeRequest);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

    /**
     * 사용자 탈퇴
     * @param user
     * @param request
     * @param response
     * @return
     */
    @PatchMapping("/delete")
    public ResponseEntity<Response<Void>> getUserProfileDetail(@CurrentUser User user,
                                                               @RequestBody MypagePasswordChangeRequest request,
                                                               HttpServletResponse response) {
        userService.deleteUser(user, request, response);
        return Response.ok(messageUtil.getMessage("response.read"),null);
    }

    /**
     * 마이페이지 비밀번호 변경
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/password-change")
    public ResponseEntity<Response<Void>> updatePasswordChange(@CurrentUser User user,
                                                               @RequestBody MypagePasswordChangeRequest request) {
        userService.updatePasswordChange(user, request);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

}
