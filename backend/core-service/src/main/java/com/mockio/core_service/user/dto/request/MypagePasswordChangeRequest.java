package com.mockio.core_service.user.dto.request;

import com.mockio.common_core.annotation.Password;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record MypagePasswordChangeRequest(
        @NotBlank(message = "{password.notBlank}",groups = Step1.class)
        String password,
        @NotBlank(message = "{password.notBlank}",groups = Step2.class)
        @Password(message = "{password.pattern}", groups = Step2.class)
        String newPassword,
        @NotBlank(message = "{password.notBlank}",groups = Step3.class)
        @Password(message = "{password.pattern}", groups = Step3.class)
        String confirmPassword
) {
}
