package com.mockio.gateway.handler;

import com.mockio.common_core.error.ErrorCode;
import com.mockio.gateway.util.GatewaySecurityErrorWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.mockio.common_core.constant.CommonErrorEnum.*;

@Component
@RequiredArgsConstructor
public class GatewayAuthenticationEntryPoint  implements ServerAuthenticationEntryPoint {

    private final GatewaySecurityErrorWriter writer;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        var errorEnum = classify(ex); // ERR_TOKEN_*

        return writer.write(exchange, HttpStatus.UNAUTHORIZED, errorEnum.getMessage(), errorEnum);
    }

    private ErrorCode classify(AuthenticationException ex) {
        if (ex instanceof OAuth2AuthenticationException oauth2Ex) {
            String desc = safeLower(oauth2Ex.getError() != null ? oauth2Ex.getError().getDescription() : null);
            String code = safeLower(oauth2Ex.getError() != null ? oauth2Ex.getError().getErrorCode() : null);

            if ("invalid_request".equals(code)) return ERR_TOKEN_MISSING;

            if ("invalid_token".equals(code) && containsAny(desc, "expired", "jwt expired", "token is expired", "exp")) {
                return ERR_TOKEN_EXPIRED;
            }

            return ERR_TOKEN_INVALID;
        }

        String msg = safeLower(ex.getMessage());
        String causeMsg = safeLower(ex.getCause() != null ? ex.getCause().getMessage() : null);

        if (containsAny(msg, "expired") || containsAny(causeMsg, "expired")) return ERR_TOKEN_EXPIRED;
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
