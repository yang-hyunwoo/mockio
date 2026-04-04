package com.mockio.core_service.user.dto.request;

import com.mockio.common_core.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record MypagePasswordChangeRequest(

        @NotBlank(message = "{password.notBlank}",groups = Step1.class)
        @Password(message = "{password.pattern}", groups = Step1.class)
        @Schema(description = "비밀번호" , example = "asdfas")
        String password,

        @NotBlank(message = "{password.notBlank}",groups = Step2.class)
        @Password(message = "{password.pattern}", groups = Step2.class)
        @Schema(description = "새 비밀번호" , example = "asdfas")
        String newPassword,

        @NotBlank(message = "{password.notBlank}",groups = Step3.class)
        @Password(message = "{password.pattern}", groups = Step3.class)
        @Schema(description = "새 비밀번호 확인" , example = "asdfas")
        String confirmPassword

) {}
