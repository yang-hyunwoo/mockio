package com.mockio.core_service.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record LoginFailureRequest(

        @NotBlank(message = "{email.notBlank}", groups = Step1.class)
        @Schema(description = "이메일" ,example = "test@naver.com")
        String email
) {}
