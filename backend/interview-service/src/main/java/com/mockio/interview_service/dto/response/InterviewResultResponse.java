package com.mockio.interview_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewResultResponse(
        Long id,
        String title,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        OffsetDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        OffsetDateTime endedAt,
        int durationSeconds,
        Integer totalCount,
        int answeredCount,
        Integer overallScore,
        EnumResponse status,
        EnumResponse track,
        EnumResponse difficulty,
        EnumResponse feedbackStyle,
        String summary,
        List<String> strengths,
        List<String> improvements,
        List<QuestionItem> questions
) {
        public record QuestionItem(
                Long id,
                Integer questionOrder,
                String question,
                String answer,
                String feedback,
                Integer score,
                EnumResponse type,
                List<String> strengths,
                List<String> improvements,
                String modelAnswer
        ) {
        }
}
