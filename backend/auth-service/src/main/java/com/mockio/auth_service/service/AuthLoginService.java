package com.mockio.auth_service.service;

import com.mockio.auth_service.config.JwtTokenProvider;
import com.mockio.auth_service.dto.request.UserLoginRequest;
import com.mockio.auth_service.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(UserLoginRequest request) {

        return new LoginResponse(
                1L,
                "test@example.com"
        );
    }


}
