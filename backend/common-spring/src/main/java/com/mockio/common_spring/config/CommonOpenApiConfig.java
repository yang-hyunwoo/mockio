package com.mockio.common_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;

import io.swagger.v3.oas.models.responses.ApiResponse;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class CommonOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Schema<?> errorSchema = new Schema<>().$ref("#/components/schemas/ErrorResponse");

        Map<String, Object> badRequestExample = new LinkedHashMap<>();
        badRequestExample.put("resultCode", "ERROR");
        badRequestExample.put("httpCode", 400);
        badRequestExample.put("message", "요청 값이 올바르지 않습니다.");
        badRequestExample.put("errCode", "VALIDATION_ERROR");
        badRequestExample.put("errCodeMsg", "email 형식이 올바르지 않습니다.");
        badRequestExample.put("data", null);
        badRequestExample.put("timestamp", "2026-04-02T14:00:00");

        Map<String, Object> unauthorizedExample = new LinkedHashMap<>();
        unauthorizedExample.put("resultCode", "ERROR");
        unauthorizedExample.put("httpCode", 401);
        unauthorizedExample.put("message", "인증에 실패했습니다.");
        unauthorizedExample.put("errCode", "AUTH_FAILED");
        unauthorizedExample.put("errCodeMsg", "이메일 또는 비밀번호가 올바르지 않습니다.");
        unauthorizedExample.put("data", null);
        unauthorizedExample.put("timestamp", "2026-04-02T14:00:00");

        Map<String, Object> InternalServerErrorExample = new LinkedHashMap<>();
        InternalServerErrorExample.put("resultCode", "INTERNAL_SERVER_ERROR");
        InternalServerErrorExample.put("httpCode", 500);
        InternalServerErrorExample.put("message", "서버 오류가 발생했습니다.");
        InternalServerErrorExample.put("errCode", "INTER_SERVER_ERROR");
        InternalServerErrorExample.put("errCodeMsg", "서버 오류가 발생했습니다.");
        InternalServerErrorExample.put("data", null);
        InternalServerErrorExample.put("timestamp", "2026-04-02T14:00:00");

        return new OpenAPI()
                .components(new Components()
                        .addResponses("BadRequest",
                                new ApiResponse()
                                        .description("요청값 오류")
                                        .content(new Content().addMediaType(
                                                "application/json",
                                                new MediaType()
                                                        .schema(errorSchema)
                                                        .example(badRequestExample)
                                        )))
                        .addResponses("Unauthorized",
                                new ApiResponse()
                                        .description("인증 실패")
                                        .content(new Content().addMediaType(
                                                "application/json",
                                                new MediaType()
                                                        .schema(errorSchema)
                                                        .example(unauthorizedExample)
                                        )))
                        .addResponses("InternalServerError",
                                new ApiResponse()
                                        .description("서버 오류")
                                        .content(new Content().addMediaType(
                                                "application/json",
                                                new MediaType()
                                                        .schema(errorSchema)
                                                        .example(InternalServerErrorExample)
                                        )))
                );
    }
}