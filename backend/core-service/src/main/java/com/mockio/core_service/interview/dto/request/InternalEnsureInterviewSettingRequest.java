package com.mockio.core_service.interview.dto.request;

import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record InternalEnsureInterviewSettingRequest(

        @Schema(description = "사용자ID" , example = "1")
        @NotBlank(message = "{default.notBlank}",groups = ValidationGroups.Step1.class)
        Long userId

) {}
