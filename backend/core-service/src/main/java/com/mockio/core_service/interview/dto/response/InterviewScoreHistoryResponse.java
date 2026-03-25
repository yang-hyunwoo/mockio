package com.mockio.core_service.interview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewScoreHistoryResponse(
       List<Item> scoreHistory
) {
    public record Item(
            Long interviewId,
            String title,
            EnumResponse track,
            int score,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM.dd")
            OffsetDateTime endedAt
    ) {}
}
