package com.mockio.common_ai_contractor.constant;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewErrorCode implements ErrorCode {

    INTERVIEW_NOT_FOUND(404, "INTERVIEW_NOT_FOUND", "면접이 존재하지 않습니다."),
    INTERVIEW_FORBIDDEN(403, "INTERVIEW_FORBIDDEN", "해당 면접에 접근 권한이 없습니다."),

    QUESTIONS_ALREADY_GENERATED(409, "QUESTIONS_ALREADY_GENERATED", "이미 질문이 생성된 면접입니다."),
    QUESTION_NOT_FOUND(404, "QUESTION_NOT_FOUND", "질문이 존재하지 않습니다."),

    INVALID_INTERVIEW_STATUS(409, "INVALID_INTERVIEW_STATUS", "면접 상태가 올바르지 않습니다."),
    ANSWER_ATTEMPT_CONFLICT(409, "ANSWER_ATTEMPT_CONFLICT", "답변 회차 충돌이 발생했습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}