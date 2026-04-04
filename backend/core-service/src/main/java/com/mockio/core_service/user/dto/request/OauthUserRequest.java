package com.mockio.core_service.user.dto.request;


import com.mockio.common_core.annotation.Email;
import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import com.mockio.core_service.user.constant.AuthProviderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record OauthUserRequest(

        @NotBlank(message = "{email.notBlank}", groups = Step1.class)
        @Email(groups = Step1.class)
        @Schema(description = "이메일" , example = "1")
        String email,

        @NotBlank(message = "{name.notBlank}", groups = Step2.class)
        @Schema(description = "이름" , example = "1")
        String name,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Schema(description = "소셜타입" , example = "GOOGLE")
        AuthProviderEnum provider,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Schema(description = "이메일" , example = "1")
        String password,

        @NotBlank(message = "{nickname.notBlank}", groups = Step2.class)
        @Schema(description = "닉네임" , example = "1")
        String nickname

) {}
