package com.mockio.user_service.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mockio.common_core.error.ErrorCode;
import com.mockio.common_spring.util.response.Response;
import com.mockio.common_spring.util.response.ResponseBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;


import java.io.IOException;

import static com.mockio.common_core.constant.CommonErrorEnum.*;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ErrorCode code = classify(authException);

        ObjectMapper om = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Response<String> error = ResponseBuilder.buildError(HttpStatus.FORBIDDEN.value(), code.getMessage(),code);
        String responseBody = om.writeValueAsString(error);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().println(responseBody);
    }

    private ErrorCode classify(AuthenticationException ex) {
        // 1) OAuth2AuthenticationException으로 들어오는 경우: OAuth2Error 활용
        if (ex instanceof OAuth2AuthenticationException oauthEx) {
            OAuth2Error err = oauthEx.getError();
            String errorCode = safeLower(err != null ? err.getErrorCode() : null);
            String desc = safeLower(err != null ? err.getDescription() : null);

            // Bearer 토큰이 없거나 형식이 잘못된 경우가 invalid_request로 오는 케이스가 많음
            if ("invalid_request".equals(errorCode)) {
                return ERR_TOKEN_MISSING;
            }

            // invalid_token + 만료 관련 문구
            if ("invalid_token".equals(errorCode) && containsAny(desc, "expired", "jwt expired", "token is expired", "exp")) {
                return ERR_TOKEN_EXPIRED;
            }

            // 그 외는 무효 토큰
            return ERR_TOKEN_INVALID;
        }

        // 2) cause에 JwtException 계열이 붙는 경우가 흔함
        Throwable cause = ex.getCause();
        if (cause instanceof JwtException jwtEx) {
            String msg = safeLower(jwtEx.getMessage());
            if (containsAny(msg, "expired", "jwt expired", "token is expired", "exp")) {
                return ERR_TOKEN_EXPIRED;
            }
            return ERR_TOKEN_INVALID;
        }

        // 3) 마지막 fallback: 메시지 기반 최소 분기
        String msg = safeLower(ex.getMessage());
        String causeMsg = safeLower(cause != null ? cause.getMessage() : null);

        if (containsAny(msg, "missing", "no bearer", "authorization header") ||
                containsAny(causeMsg, "missing", "no bearer", "authorization header")) {
            return ERR_TOKEN_MISSING;
        }

        if (containsAny(msg, "expired") || containsAny(causeMsg, "expired")) {
            return ERR_TOKEN_EXPIRED;
        }

        return ERR_TOKEN_INVALID;
    }

    private static boolean containsAny(String text, String... keys) {
        if (text == null) return false;
        for (String k : keys) if (text.contains(k)) return true;
        return false;
    }

    private static String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }

}
