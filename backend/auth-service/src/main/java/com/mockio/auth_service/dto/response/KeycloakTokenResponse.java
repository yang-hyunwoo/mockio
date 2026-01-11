package com.mockio.auth_service.dto.response;

/**
 * Keycloak 토큰 발급/갱신 응답을 표현하는 DTO.
 *
 * <p>OpenID Connect 토큰 엔드포인트에서 반환되는
 * Access Token, Refresh Token 및 만료 정보를 매핑한다.</p>
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Keycloak 토큰 응답")
public record KeycloakTokenResponse(

        @Schema(description = "Access Token")
        @JsonProperty("access_token")
        String accessToken,

        @Schema(description = "Refresh Token")
        @JsonProperty("refresh_token")
        String refreshToken,

        @Schema(description = "토큰 타입", example = "Bearer")
        @JsonProperty("token_type")
        String tokenType,

        @Schema(description = "Access Token 만료 시간(초)", example = "300")
        @JsonProperty("expires_in")
        Long expiresIn,

        @Schema(description = "Refresh Token 만료 시간(초)", example = "2592000")
        @JsonProperty("refresh_expires_in")
        Long refreshExpiresIn
) {}
