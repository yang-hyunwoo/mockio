package com.mockio.auth_service.dto.response;

/**
 * Keycloak 오류 응답을 표현하는 DTO.
 *
 * <p>Keycloak OpenID Connect 토큰 엔드포인트에서
 * 오류 발생 시 반환되는 error / error_description 필드를 매핑한다.</p>
 */

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Keycloak 오류 응답")
public record KeycloakErrorResponse(

        @Schema(description = "오류 코드", example = "invalid_grant")
        String error,

        @Schema(description = "오류 상세 설명", example = "Invalid refresh token")
        String error_description
) {}