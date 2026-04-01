package com.mockio.auth_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 인증 정보 응답 DTO
 */

public record UserAuthInfoResponse(

        @Schema(name = "사용자_ID", example = "1")
        Long id,

        @Schema(name = "이메일", example = "test@test.com")
        String email,

        @Schema(name = "이름", example = "홍길동")
        String name,

        @Schema(name = "비밀번호", example = "sadfasdf")
        String password,

        @Schema(name = "권한", example = "USER")
        String role,

        @Schema(name = "로그인_실패_횟수", example = "1")
        int failLoginCount,

        @Schema(name = "사용자_상태", example = "ACTIVE")
        String status

) {}
