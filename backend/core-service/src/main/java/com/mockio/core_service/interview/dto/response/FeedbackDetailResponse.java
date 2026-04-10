package com.mockio.core_service.interview.dto.response;

/**
 * 피드백 상세 응답 DTO
 */

import com.mockio.common_ai_contractor.generator.feedback.FeedbackDimensions;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackJobMetric;
import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FeedbackDetailResponse(

        @Schema(description = "ID", example = "1")
        Long id,

        @Schema(description = "질문ID", example = "1")
        Long answerId,

        @Schema(description = "점수", example = "30")
        Integer score,

        @Schema(description = "피드백 요약", example = "이건 어쩌고...")
        String summary,

        @Schema(description = "강점", example = "[강점1,강점2]")
        List<String> strengths,

        @Schema(description = "보안점", example = "[보안,보안2]")
        List<FeedbackImprovement> improvements,

        @Schema(description = "적절한 답변", example = "요건 어쩌고...")
        String modelAnswer,

        @Schema(description = "상태", example = "COMPLETED")
        EnumResponse status,

        @Schema(description = "구조,명확,구체", example = "0")
        FeedbackDimensions dimensions,

        @Schema(description = "중요말", example = "히")
        String headline,

        @Schema(description = "보안점태그", example = "[]")
        List<String> improvementTags,

        @Schema(description = "실무적합성", example = "0")
        FeedbackJobMetric jobMetrics

) {}

