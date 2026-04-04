package com.mockio.common_ai_contractor.generator.feedback;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 면접 총 평가 피드백 DTO
 */

public record GeneratedSummaryFeedback(

        @Schema(description = "면접 ID", example = "1")
        Long interviewId,

        @Schema(description = "평가 답변", example = "평가 답변")
        String summaryFeedbackText,

        @Schema(description = "총합", example = "10")
        Integer totalScore,

        @Schema(description = "ai", example = "OPENAI")
        String provider,

        @Schema(description = "ai 모델", example = "gpt")
        String model,

        @Schema(description = "ai 버전", example = "1.0")
        String promptVersion,

        @Schema(description = "연관도", example = "0.2")
        Double temperature

) {}