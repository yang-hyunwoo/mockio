package com.mockio.user_service.dto.response;

import com.mockio.user_service.constant.AuthProviderEnum;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignupResponse(

        @Schema(description = "사용자pk", example = "1")
        Long id,

        @Schema(description = "닉네임", example = "axs")
        String nickName,

        @Schema(description = "소셜타입", example = "NORMAL")
        AuthProviderEnum provider
) {

}
