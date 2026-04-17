package com.mockio.support_service.questionboard.dto.internal.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserInfoResponse(

        @Schema(description = "사용자 ID" , example = "1")
        Long id,

        @Schema(description = "이메일" , example = "test@naver.com")
        String email,

        @Schema(description = "닉네임" , example = "호호")
        String nickname

) {}
