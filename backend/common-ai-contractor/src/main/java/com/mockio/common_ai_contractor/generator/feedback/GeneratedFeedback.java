package com.mockio.common_ai_contractor.generator.feedback;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * follow up ai DTO
 */

public record GeneratedFeedback(

        @Schema(description = "피드백", example = "피드백")
        String feedbackText,

        @Schema(description = "점수", example = "10")
        Integer score,

        @Schema(description = "ai", example = "OPENAI")
        String provider,

        @Schema(description = "ai 모델", example = "gpt")
        String model,

        @Schema(description = "ai 버전", example = "1.0")
        String promptVersion,

        @Schema(description = "연관도", example = "0.2")
        Double temperature
) {}
