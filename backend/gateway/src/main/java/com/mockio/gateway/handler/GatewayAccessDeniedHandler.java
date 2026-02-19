package com.mockio.gateway.handler;


import com.mockio.gateway.util.GatewaySecurityErrorWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_403;

@Component
@RequiredArgsConstructor
public class GatewayAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final GatewaySecurityErrorWriter writer;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, org.springframework.security.access.AccessDeniedException ex) {
        return writer.write(exchange, HttpStatus.FORBIDDEN, ERR_403.getMessage(), ERR_403);
    }

}
