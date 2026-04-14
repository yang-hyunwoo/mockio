package com.mockio.core_service.interview.dto.response;

import com.mockio.common_ai_contractor.generator.feedback.FeedbackDimensions;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackJobMetric;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

public record CompareInterviewResponse(

        @Schema(description = "현재 면접", example = "CompareItem")
        CompareItem currentInterview,

        @Schema(description = "이전 면접", example = "CompareItem")
        CompareItem previousInterview,

        @Schema(description = "비교 면접 item", example = "[]")
        List<QuestionCompareItem> questionItem
) {
    public record CompareItem(

            @Schema(description = "면접 ID", example = "1")
            Long interviewId,

            @Schema(description = "총점", example = "1")
            Integer overallScore,

            @Schema(description = "면접 구조도", example = "[]")
            FeedbackDimensions dimensions,

            @Schema(description = "면접 실무력", example = "[]")
            FeedbackJobMetric jobMetrics,

            @Schema(description = "종료일", example = "2026-04-01")
            OffsetDateTime endedAt
    ) {}

    public record QuestionCompareItem(

            @Schema(description = "현재 면접 ID", example = "1")
            Long currentQuestionId,

            @Schema(description = "이전 면접 ID", example = "1")
            Long prevQuestionId,

            @Schema(description = "면접 질문", example = "JPA가 뭔가요")
            String title,

            @Schema(description = "순번", example = "10")
            Integer seq,

            @Schema(description = "이전 답변", example = "이전 답변입니다.")
            String previousAnswer,

            @Schema(description = "현재 답변", example = "현재 답변입니다.")
            String currentAnswer,

            @Schema(description = "이전 점수", example = "10")
            Integer previousScore,

            @Schema(description = "현재 점수", example = "10")
            Integer currentScore
    ) {}

}
