package com.mockio.core_service.interview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

/**
 * 면접 재 생성 요청 DTO
 * @param idempotencyKey
 * @param interviewId
 */

public record RetryInterviewRequest(

        @NotBlank(message = "{default.notBlank}",groups = Step1.class)
        @Schema(description = "멱등성키" , example = "sdfsadf")
        String idempotencyKey,

        @NotBlank(message = "{default.notBlank}",groups = Step2.class)
        @Schema(description = "면접ID" , example = "1")
        Long interviewId

) {}
