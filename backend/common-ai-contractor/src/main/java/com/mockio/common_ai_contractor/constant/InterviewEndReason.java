package com.mockio.common_ai_contractor.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewEndReason {
    COMPLETED("완료"),
    USER_EXIT("사용자_종료"),
    SYSTEM_EXIT("시스템_종료"),
    ERROR("에러")
    ;

    private final String label;

}
