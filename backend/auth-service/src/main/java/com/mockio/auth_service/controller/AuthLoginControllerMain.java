package com.mockio.auth_service.controller;

import com.mockio.auth_service.dto.UserInfoResponse;
import com.mockio.auth_service.dto.request.UserLoginRequest;
import com.mockio.auth_service.dto.response.LoginResponse;
import com.mockio.auth_service.service.AuthLoginService;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthLoginControllerMain {

    private final AuthLoginService authLoginService;
    private final MessageUtil messageUtil;

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody UserLoginRequest request,
                                               HttpServletResponse response) {
        LoginResponse login = authLoginService.login(request, response);
        return Response.ok(messageUtil.getMessage("response.read"),login);
    }

    @GetMapping("/me")
    public ResponseEntity<Response<UserInfoResponse>> me(@CurrentSubject Long id) {
        return Response.ok(messageUtil.getMessage("response.read"),authLoginService.userDetail(id));
    }

    @GetMapping("/public/refresh")
    public ResponseEntity<Response<LoginResponse>> refresh(HttpServletRequest request,
                                                           HttpServletResponse response) {
        return Response.ok(messageUtil.getMessage("response.read"),authLoginService.refresh(request,response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authLoginService.logout(request, response);
        return Response.ok(messageUtil.getMessage("response.read"),null);
    }

}
