package com.mockio.auth_service.dto.request;

/**
 * Keycloak 사용자 정보 수정 요청 DTO.
 *
 * <p>관리자 권한을 통해 사용자 계정의 활성/비활성 상태를 변경할 때 사용된다.</p>
 */

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Keycloak 사용자 수정 요청")
public record UserUpdateRequest(

        @Schema(description = "사용자 활성화 여부", example = "false")
        Boolean enabled
) {}
