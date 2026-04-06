package com.mockio.common_ai_contractor.constant;

/**
 * 면접 상태 enum
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewStatus {
    PENDING("대기"),
    GENERATING("생성중"),
    ACTIVE("질문 생성 완료"),
    ENDED("완료"),
    FAILED("실패")
    ;

    private final String label;

}
