package com.mockio.interview_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionType {

    BASE("기본"),
    FOLLOW_UP("꼬리 질문")
    ;



    private final String label;
}
