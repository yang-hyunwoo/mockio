package com.mockio.core_service.interview.dto.request;

/**
 * 인터뷰 답변 요청 DTO
 */


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

public record InterviewAnswerRequest(

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(name = "인터뷰_ID", example = "1")
        Long interviewId,

        @NotBlank(message = "{default.notBlank}", groups = Step2.class)
        @Schema(name = "질문_ID", example = "1")
        Long questionId,

        @NotBlank(message = "{default.notBlank}", groups = Step3.class)
        @Schema(name = "답변", example = "아아라라")
        String answerText,

        @NotBlank(message = "{default.notBlank}", groups = Step4.class)
        @Schema(name = "타이핑 시간", example = "30")
        Integer answerDurationSeconds,

        @NotBlank(message = "{default.notBlank}", groups = Step5.class)
        @Schema(name = "멱등성 키", example = "uuid")
        String idempotencyKey
) {}
