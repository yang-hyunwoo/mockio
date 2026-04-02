package com.mockio.common_ai_contractor.constant;

/**
 * 면접 상태 enum
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewStatus {
    ACTIVE("진행중"),
    ENDED("완료"),
    FAILED("실패")
    ;

    private final String label;

}
