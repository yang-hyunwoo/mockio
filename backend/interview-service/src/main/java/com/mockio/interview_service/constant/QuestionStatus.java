package com.mockio.interview_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionStatus {
    READY("준비"),
    ASKED("질문"),
    ANSWERED("답변 완료"),
    SKIPPED("넘기기")
    ;

    private final String label;

}
