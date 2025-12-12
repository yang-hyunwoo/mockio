package com.mockio.gateway.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mockio.common_core.error.ErrorCode;
import com.mockio.common_spring.constant.CommonErrorEnum;
import com.mockio.common_spring.util.response.Response;
import com.mockio.common_spring.util.response.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

@Component
public class GatewaySecurityErrorWriter {

    private final ObjectMapper objectMapper;

    public GatewaySecurityErrorWriter() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    public Mono<Void> write(ServerWebExchange exchange ,
                            HttpStatus status ,
                            String message,
                            ErrorCode errorCode) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(APPLICATION_JSON);

        Response<String> body = ResponseBuilder.buildError(status.value(), message,errorCode);

        try {
            byte[] bytes = objectMapper.writeValueAsString(body).getBytes(UTF_8);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            byte[] fallback = ("{\"resultCode\":\"ERROR\",\"httpCode\":" + status.value() +
                    ",\"message\":\"" + message + "\"}").getBytes(UTF_8);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(fallback)));
        }
    }


}
