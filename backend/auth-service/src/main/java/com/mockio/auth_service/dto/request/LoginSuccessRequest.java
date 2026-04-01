package com.mockio.auth_service.dto.request;

/**
 * 로그인 성공 요청 DTO
 */

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginSuccessRequest(

        @Schema(name = "사용자_ID", example = "1")
        Long userId

) {}
