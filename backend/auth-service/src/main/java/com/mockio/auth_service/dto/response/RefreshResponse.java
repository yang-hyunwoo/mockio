package com.mockio.auth_service.dto.response;

/**
 * Access Token 재발급(refresh) 응답 DTO.
 *
 * <p>Refresh Token은 HttpOnly 쿠키로만 관리되며,
 * 이 응답은 새로 발급된 Access Token과 만료 시간만 포함한다.</p>
 */

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Access Token 재발급 응답")
public record RefreshResponse(

        @Schema(description = "새로 발급된 Access Token")
        String accessToken,

        @Schema(description = "Access Token 만료 시간(초)", example = "300")
        Long expiresIn
) {}
