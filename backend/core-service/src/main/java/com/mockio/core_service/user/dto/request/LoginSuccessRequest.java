package com.mockio.core_service.user.dto.request;

import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginSuccessRequest(

        @NotBlank(message = "{default.notBlank}", groups = ValidationGroups.Step1.class)
        @Schema(description = "사용자 ID" , example = "1")
        Long userId

) {}
