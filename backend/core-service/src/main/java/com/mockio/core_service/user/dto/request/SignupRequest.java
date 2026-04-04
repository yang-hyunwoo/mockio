package com.mockio.core_service.user.dto.request;

import com.mockio.common_core.annotation.Email;
import com.mockio.common_core.annotation.Password;
import com.mockio.common_core.annotation.Sanitize;
import com.mockio.common_spring.annotation.KoreanEnglish;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record SignupRequest(

        @NotBlank(message = "{email.notBlank}",groups = Step1.class)
        @Email(groups = Step1.class)
        @Schema(description = "이메일", example = "test@naver.com")
        String email,

        @NotBlank(message = "{nickname.notBlank}", groups = Step3.class)
        @KoreanEnglish(min = 1, max = 30, message = "{nickname.pattern}",messageKey = "nickname.range", groups = Step3.class)
        @Sanitize(groups = Step2.class)
        @Schema(description = "닉네임", example = "호호")
        String nickname,

        @NotBlank(message = "{password.notBlank}",groups = Step3.class)
        @Password(message = "{password.pattern}", groups = Step3.class)
        @Schema(description = "비밀번호", example = "14334")
        String password,

        @Schema(description = "리캡차_토큰", example = "1asdf434")
        String recaptchaToken

) {}
