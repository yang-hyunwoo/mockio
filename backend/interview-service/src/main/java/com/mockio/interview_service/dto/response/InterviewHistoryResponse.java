package com.mockio.interview_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_spring.util.response.EnumResponse;

import java.time.OffsetDateTime;
import java.util.List;

public record InterviewHistoryResponse(
       List<Item> historyItems
) {
    public record Item(
            Long interviewId,
            String title,
            int questionCount,
            EnumResponse status,
            EnumResponse endReason,
            EnumResponse track,
            int score,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            OffsetDateTime createdAt
    ) {}

}
