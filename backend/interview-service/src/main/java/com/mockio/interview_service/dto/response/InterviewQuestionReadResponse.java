package com.mockio.interview_service.dto.response;

import com.mockio.common_spring.util.response.EnumResponse;

import java.util.List;
import java.util.Set;

public record InterviewQuestionReadResponse(
        List<Item> questions,
        Long interviewId,
        boolean completed
) {
    public record Item(
            Long id,
            Long interviewId,
            Integer seq,
            String title,
            String questionText,
            Set<String> tags,
            EnumResponse type
    ) {}
}
