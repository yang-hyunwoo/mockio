package com.mockio.auth_service.controller;

import com.mockio.auth_service.dto.request.UserLoginRequest;
import com.mockio.auth_service.dto.response.LoginResponse;
import com.mockio.auth_service.service.AuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/v1/public")
@RequiredArgsConstructor
public class AuthLoginControllerMain {

    private final AuthLoginService authLoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(authLoginService.login(request));
    }
}
