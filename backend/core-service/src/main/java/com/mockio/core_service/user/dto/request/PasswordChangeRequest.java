package com.mockio.core_service.user.dto.request;

import com.mockio.common_core.annotation.Password;
import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
        @NotBlank(message = "{token.notBlank}",groups = ValidationGroups.Step2.class)
        String token,
        @NotBlank(message = "{password.notBlank}",groups = ValidationGroups.Step2.class)
        @Password(message = "{password.pattern}", groups = ValidationGroups.Step2.class)
        String password,
        @NotBlank(message = "{password.notBlank}",groups = ValidationGroups.Step2.class)
        @Password(message = "{password.pattern}", groups = ValidationGroups.Step2.class)
        String confirmPassword
) {}
