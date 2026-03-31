package com.mockio.core_service.util.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mockio.common_core.error.ErrorCode;
import com.mockio.common_spring.util.response.Response;
import com.mockio.common_spring.util.response.ResponseBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.mockio.common_core.constant.CommonErrorEnum.*;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorCode code = classify(authException);

        Response<String> error = ResponseBuilder.buildError(
                HttpStatus.UNAUTHORIZED.value(),
                code.getMessage(),
                code
        );

        String responseBody = objectMapper.writeValueAsString(error);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(responseBody);
    }

    private ErrorCode classify(AuthenticationException ex) {

        if (ex instanceof InsufficientAuthenticationException) {
            return ERR_TOKEN_MISSING;
        }

        if (ex instanceof OAuth2AuthenticationException oauthEx) {
            OAuth2Error err = oauthEx.getError();
            String errorCode = safeLower(err != null ? err.getErrorCode() : null);
            String desc = safeLower(err != null ? err.getDescription() : null);

            if ("invalid_request".equals(errorCode)) {
                return ERR_TOKEN_MISSING;
            }

            if ("invalid_token".equals(errorCode) &&
                    containsAny(desc, "expired", "jwt expired", "token is expired", "exp")) {
                return ERR_TOKEN_EXPIRED;
            }

            return ERR_TOKEN_INVALID;
        }

        Throwable cause = ex.getCause();
        if (cause instanceof JwtException jwtEx) {
            String msg = safeLower(jwtEx.getMessage());
            if (containsAny(msg, "expired")) {
                return ERR_TOKEN_EXPIRED;
            }
            return ERR_TOKEN_INVALID;
        }

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
