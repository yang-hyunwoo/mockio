package com.mockio.core_service.interview.dto.response;

/**
 * 면접 결과 조회 응답 DTO
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackDimensions;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackJobMetric;
import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewResultResponse(

        @Schema(description = "면접ID" , example = "1")
        Long id,

        @Schema(description = "제목" , example = "제목입니다.")
        String title,

        @Schema(description = "시작일" , example = "2026-03-03")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        OffsetDateTime createdAt,

        @Schema(description = "종료일" , example = "2026-03-04")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        OffsetDateTime endedAt,

        @Schema(description = "총면접시간" , example = "150")
        int durationSeconds,

        @Schema(description = "면접질문갯수" , example = "10")
        Integer totalCount,

        @Schema(description = "면접답변갯수" , example = "10")
        int answeredCount,

        @Schema(description = "점수" , example = "10")
        Integer overallScore,

        @Schema(description = "면접상태" , example = "ACTIVE")
        EnumResponse status,

        @Schema(description = "면접트랙" , example = "HR")
        EnumResponse track,

        @Schema(description = "면접난이도" , example = "EASY")
        EnumResponse difficulty,

        @Schema(description = "면접피드백" , example = "STRICT")
        EnumResponse feedbackStyle,

        @Schema(description = "요약" , example = "요약")
        String summary,

        @Schema(description = "강점" , example = "[]")
        List<String> strengths,

        @Schema(description = "약점" , example = "[]")
        List<String> improvements,

        @Schema(description = "질문리스트" , example = "[]")
        List<QuestionItem> questions,

        @Schema(description = "구조,명확,구체" , example = "")
        FeedbackDimensions feedbackDimensions,

        @Schema(description = "실무 적합도" , example = "")
        FeedbackJobMetric feedbackJobMetrics,

        @Schema(description = "종료사유" , example = "USER_EXIT")
        EnumResponse endReason,

        @Schema(description = "이전_면접_ID" , example = "1")
        Long sourceInterviewId


) {
        public record QuestionItem(

                @Schema(description = "면접ID" , example = "1")
                Long id,

                @Schema(description = "질문순번" , example = "1")
                Integer questionOrder,

                @Schema(description = "질문" , example = "질문")
                String question,

                @Schema(description = "답변" , example = "답변")
                String answer,

                @Schema(description = "피드백" , example = "피드백")
                String feedback,

                @Schema(description = "점수" , example = "10")
                Integer score,

                @Schema(description = "타입" , example = "1")
                EnumResponse type,

                @Schema(description = "강점" , example = "[]")
                List<String> strengths,

                @Schema(description = "보안점" , example = "[]")
                List<FeedbackImprovement> improvements,

                @Schema(description = "ai답변" , example = "ai답입니다.")
                String modelAnswer,

                @Schema(description = "구조,명확,구체" , example = "")
                FeedbackDimensions dimensions,

                @Schema(description = "강조문구" , example = "강조문구")
                String headline,

                @Schema(description = "약점" , example = "[]")
                List<String> improvementTags,

                @Schema(description = "실무 적합도" , example = "")
                FeedbackJobMetric jobMetrics

        ) {}
}
