package com.mockio.common_ai_contractor.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewStatus {
    ACTIVE("사용중"),
    ENDED("완료"),
    FAILED("실패")
    ;

    private final String label;
}
