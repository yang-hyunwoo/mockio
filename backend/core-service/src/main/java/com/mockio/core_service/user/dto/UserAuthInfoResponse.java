package com.mockio.core_service.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 정보 응답 DTO
 * @param id
 * @param name
 * @param email
 * @param password
 * @param role
 * @param failLoginCount
 * @param status
 */

public record UserAuthInfoResponse(

        @Schema(description = "사용자 ID" , example = "1")
        Long id,

        @Schema(description = "이름" , example = "홍길동")
        String name,

        @Schema(description = "이메일" , example = "test@naver.com")
        String email,

        @Schema(description = "비밀번호" , example = "assadf")
        String password,

        @Schema(description = "권한" , example = "USER")
        String role,

        @Schema(description = "로그인 실패 횟수" , example = "1")
        int failLoginCount,

        @Schema(description = "상태" , example = "ACTIVE")
        String status
) {}
