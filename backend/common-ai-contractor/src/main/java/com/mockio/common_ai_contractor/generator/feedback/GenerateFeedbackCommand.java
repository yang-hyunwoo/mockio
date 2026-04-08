package com.mockio.common_ai_contractor.generator.feedback;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import static com.mockio.common_core.annotation.otherValidator.ValidationGroups.*;

/**
 * 면접 피드백 DTO
 */

public record GenerateFeedbackCommand(

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(description = "질문", example = "질문")
        String questionText,

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(description = "답변", example = "답변")
        String answerText,

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(description = "면접 트랙", example = "HR")
        String track,

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(description = "면접 난이도", example = "EAST")
        String difficulty,

        @NotBlank(message = "{default.notBlank}", groups = Step1.class)
        @Schema(description = "피드백 스타일", example = "STRICT")
        String feedbackStyle,

        @Schema(description = "질문 태그", example = "REST")
        String primaryTag

) {}

