package com.mockio.interview_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuestionGenerationStatus {
    NONE("아직 생성 안 함"),
    GENERATING("생성 중"),
    DONE("생성 완료"),
    FAILED("생성 실패"),
    ;

    private final String label;
}
