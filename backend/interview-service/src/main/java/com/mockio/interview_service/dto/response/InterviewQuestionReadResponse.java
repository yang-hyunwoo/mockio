package com.mockio.interview_service.dto.response;

import java.util.List;

public record InterviewQuestionReadResponse(
        List<Item> questions
) {
    public record Item(
            Long id,
            Integer seq,
            String questionText
    ) {}
}
