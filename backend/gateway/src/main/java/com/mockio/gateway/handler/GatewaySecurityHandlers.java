package com.mockio.gateway.handler;


import com.mockio.gateway.util.GatewaySecurityErrorWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import static com.mockio.common_core.constant.CommonErrorEnum.*;

@Component
@RequiredArgsConstructor
public class GatewaySecurityHandlers {

    private final GatewaySecurityErrorWriter writer;

    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (ServerWebExchange exchange, AuthenticationException ex) ->
                writer.write(
                        exchange,
                        HttpStatus.UNAUTHORIZED,
                        ERR_401.getMessage(),
                        ERR_401
                );
    }

    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (ServerWebExchange exchange, AccessDeniedException ex) ->
                writer.write(
                        exchange,
                        HttpStatus.FORBIDDEN,
                        ERR_403.getMessage(),
                        ERR_403
                );
    }

}
