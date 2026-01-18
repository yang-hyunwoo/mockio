package com.mockio.common_ai_contractor.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewFeedbackStatus {
    NONE("요청 전"),
    REQUESTED("요청"),
    RUNNING("진행중"),
    SUCCEEDED("완료"),
    FAILED("실패"),
    ;

    private final String label;
}
