package com.mockio.interview_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewStatus {
    CREATED("생성"),
    IN_PROGRESS("진행"),
    COMPLETED("완료"),
    FAILED("실패"),
    ;

    private final String label;
}
