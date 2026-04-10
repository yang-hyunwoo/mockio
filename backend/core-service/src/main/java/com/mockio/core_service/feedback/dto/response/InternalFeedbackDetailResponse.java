package com.mockio.core_service.feedback.dto.response;

import com.mockio.common_ai_contractor.generator.feedback.FeedbackJobMetric;
import com.mockio.common_spring.util.response.EnumResponse;

import java.util.List;

public record InternalFeedbackDetailResponse(
        Long id,
        Long answerId,
        Integer score,
        String summary,
        List<String> strengths,
        List<InternalFeedbackImprovement> improvements,
        String modelAnswer,
        EnumResponse status,
        InternalFeedbackDimensions dimensions,
        String headline,
        List<String> improvementTags,
        FeedbackJobMetric jobMetrics
) {}
