package com.mockio.core_service.interview.dto.request;

import com.mockio.common_core.annotation.otherValidator.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 면접 생성 요청 DTO
 * @param idempotencyKey
 */

public record StartInterviewRequest(

        @NotBlank(message = "{default.notBlank}",groups = ValidationGroups.Step1.class)
        @Schema(description = "멱등성키" , example = "sdfsadf")
        String idempotencyKey

) {}
