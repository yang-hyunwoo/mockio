package com.mockio.auth_service.controller;

/**
 * 인증(Authentication) 관련 API 컨트롤러
 *
 * 로그인, 사용자 정보 조회, 토큰 재발급, 로그아웃 기능을 제공한다.
 * 내부적으로 AuthLoginService를 통해 인증 로직을 수행하며,
 * 공통 응답 포맷(Response)과 메시지(MessageUtil)를 사용한다.
 */

import com.mockio.auth_service.dto.response.UserInfoResponse;
import com.mockio.auth_service.dto.request.UserLoginRequest;
import com.mockio.auth_service.dto.response.LoginResponse;
import com.mockio.auth_service.service.AuthLoginService;
import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
public class AuthLoginController {

    private final AuthLoginService authLoginService;
    private final MessageUtil messageUtil;

    /**
     * 로그인 API
     *
     * 사용자 로그인 요청을 처리하고,
     * Access/Refresh 토큰을 생성하여 응답 헤더 또는 쿠키에 설정한다.
     */
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody UserLoginRequest request,
                                               HttpServletResponse response) {
        LoginResponse login = authLoginService.login(request, response);
        return Response.ok(messageUtil.getMessage("response.read"),login);
    }

    /**
     * 현재 로그인 사용자 정보 조회 API
     *
     * @CurrentSubject를 통해 인증된 사용자 ID를 주입받아
     * 사용자 상세 정보를 반환한다.
     */
    @GetMapping("/me")
    public ResponseEntity<Response<UserInfoResponse>> me(@CurrentSubject Long id) {
        return Response.ok(messageUtil.getMessage("response.read"),authLoginService.userDetail(id));
    }

    /**
     * 토큰 재발급 API
     *
     * 요청에 포함된 Refresh Token을 기반으로
     * 새로운 Access/Refresh 토큰을 발급한다.
     */
    @GetMapping("/public/refresh")
    public ResponseEntity<Response<LoginResponse>> refresh(HttpServletRequest request) {
        return Response.ok(messageUtil.getMessage("response.read"), authLoginService.refresh(request));
    }

    /**
     * 로그아웃 API
     *
     * Refresh Token을 무효화하고,
     * 클라이언트에 저장된 인증 정보를 제거한다.
     */
    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest request,
                                                 HttpServletResponse response
    ) {
        authLoginService.logout(request, response);
        return Response.ok(messageUtil.getMessage("response.read"), null);
    }

}
