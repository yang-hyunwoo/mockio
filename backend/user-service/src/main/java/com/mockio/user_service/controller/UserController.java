package com.mockio.user_service.controller;

import com.mockio.user_service.dto.UserAuthInfoResponse;
import com.mockio.user_service.dto.request.LoginFailureRequest;
import com.mockio.user_service.dto.request.LoginSuccessRequest;
import com.mockio.user_service.dto.response.UserInfoResponse;
import com.mockio.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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



}
