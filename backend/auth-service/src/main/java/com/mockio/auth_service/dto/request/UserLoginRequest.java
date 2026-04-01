package com.mockio.auth_service.dto.request;

/**
 * 로그인 요청 DTO
 */

import com.mockio.common_core.annotation.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record UserLoginRequest(

        @NotBlank(message = "{email.notBlank}",groups = Step1.class)
        @Email(groups = Step1.class)
        @Schema(name = "이메일", example = "test@google.com")
        String email,

        @NotBlank(message = "{password.notBlank}",groups = Step2.class)
        @Schema(name = "비밀번호", example = "sadfsdf")
        String password,

        @Schema(name = "자동 로그인 여부", example = "true")
        boolean chk

) {}
