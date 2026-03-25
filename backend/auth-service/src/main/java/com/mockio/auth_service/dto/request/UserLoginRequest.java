package com.mockio.auth_service.dto.request;

import com.mockio.common_core.annotation.Email;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record UserLoginRequest(
        @NotBlank(message = "{email.notBlank}",groups = Step1.class)
        @Email(groups = Step1.class)
        String email,
        @NotBlank(message = "{password.notBlank}",groups = Step2.class)
        String password,
        boolean chk
) {}
