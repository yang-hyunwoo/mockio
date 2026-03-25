package com.mockio.auth_service.util;


import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_spring.util.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice(basePackages = "com.mockio.auth_service")
public class AuthExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Response<String>> handleLockedException(LockedException e) {
        log.error("계정 잠김", e);
        return Response.error(
                423,
                "로그인 실패 횟수 초과로 계정이 잠겼습니다.",
                CommonErrorEnum.ERR_401,
                null
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Response<String>> handleDisabledException(DisabledException e) {
        log.error("비활성 계정", e);
        return Response.error(
                FORBIDDEN.value(),
                "비활성화된 계정입니다.",
                CommonErrorEnum.ERR_403,
                null
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<String>> handleBadCredentialsException(BadCredentialsException e) {
        log.error("인증 실패", e);
        return Response.error(
                UNAUTHORIZED.value(),
                "아이디 또는 비밀번호가 올바르지 않습니다.",
                CommonErrorEnum.ERR_401,
                null
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Response<String>> handleAuthException(AuthenticationException e) {
        log.error("인증 에러", e);
        return Response.error(
                UNAUTHORIZED.value(),
                "인증되지 않았습니다.",
                CommonErrorEnum.ERR_401,
                null
        );
    }

}
