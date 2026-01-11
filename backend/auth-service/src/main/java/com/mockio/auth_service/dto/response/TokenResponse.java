package com.mockio.auth_service.dto.response;

/**
 * Keycloak 토큰 발급 응답 DTO.
 *
 * <p>Client Credentials Grant 또는 기타 토큰 발급 요청에 대한
 * Access Token 응답을 매핑한다.</p>
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Keycloak 토큰 발급 응답")
public record TokenResponse(

        @Schema(description = "발급된 Access Token")
        @JsonProperty("access_token")
        String accessToken,

        @Schema(description = "토큰 타입", example = "Bearer")
        @JsonProperty("token_type")
        String tokenType,

        @Schema(description = "Access Token 만료 시간(초)", example = "300")
        @JsonProperty("expires_in")
        long expiresIn
) {}
