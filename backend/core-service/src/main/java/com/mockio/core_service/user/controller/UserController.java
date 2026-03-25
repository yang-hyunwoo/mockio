package com.mockio.core_service.user.controller;

import com.mockio.common_security.annotation.CurrentUser;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.user.domain.User;
import com.mockio.core_service.user.dto.UserAuthInfoResponse;
import com.mockio.core_service.user.dto.request.*;
import com.mockio.core_service.user.dto.response.UserInfoResponse;
import com.mockio.core_service.user.dto.response.UserProfileDetailResponse;
import com.mockio.core_service.user.service.PasswordResetTokenService;
import com.mockio.core_service.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageUtil messageUtil;

    private final JavaMailSender mailSender;
    private final PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/internal/login/{email}")
    public UserAuthInfoResponse getUserAuthInfo(@PathVariable String email) {
        return userService.getUserAuthInfo(email);
    }

    @GetMapping("/internal/user-info/{userId}")
    public UserInfoResponse userDetail(@PathVariable Long userId) {
        return userService.userDetail(userId);
    }

    @PatchMapping("/internal/login-success")
    public void resetFailCount(@RequestBody LoginSuccessRequest loginSuccessRequest) {
         userService.resetFailCount(loginSuccessRequest.userId());
    }

    @PatchMapping("/internal/login-failure")
    public void loginFailure(@RequestBody LoginFailureRequest loginFailureRequest) {
        userService.loginFailure(loginFailureRequest.email());
    }

    @PostMapping("/internal/oauth-login")
    public UserAuthInfoResponse oauthLogin(@RequestBody OauthUserRequest oauthUserRequest) {
        return userService.oauthLogin(oauthUserRequest);
    }

    @PostMapping("/public/password-find")
    public void passwordFind(@RequestBody PasswordFindRequest passwordFindRequest) {
        userService.sendPasswordEmail(passwordFindRequest.email().trim());
    }

    @GetMapping("/public/password/reset/validate")
    public ResponseEntity<Response<Void>> validateToken(@RequestParam String token) {
        passwordResetTokenService.validateToken(token);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

    @PostMapping("/public/password/change")
    public ResponseEntity<Response<Void>> resetPasswordChange(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        userService.resetPasswordChange(passwordChangeRequest);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

    @PatchMapping("/delete")
    public ResponseEntity<Response<Void>> getUserProfileDetail(@CurrentUser User user,
                                                               @RequestBody MypagePasswordChangeRequest MypagePasswordChangeRequest,
                                                               HttpServletResponse response) {
        userService.deleteUser(user,MypagePasswordChangeRequest, response );
        return Response.ok(messageUtil.getMessage("response.read"),null);
    }

    @PostMapping("/password-change")
    public ResponseEntity<Response<Void>> updatePasswordChange(@CurrentUser User user,
                                                               @RequestBody MypagePasswordChangeRequest request) {
       userService.updatePasswordChange(user,request);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

}
