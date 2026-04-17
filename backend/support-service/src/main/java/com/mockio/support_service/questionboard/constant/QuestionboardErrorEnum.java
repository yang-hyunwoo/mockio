package com.mockio.support_service.questionboard.constant;

import com.mockio.common_core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum QuestionboardErrorEnum implements ErrorCode {

    ILLEGAL_STATE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ILLEGAL_STATE", "처리 중 알 수 없는 오류가 발생했습니다."),
    QUESTION_BOARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "QUESTION_BOARD_NOT_FOUND", "게시글을 찾을 수 없습니다."),
    QUESTION_BOARD_NOT_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "QUESTION_BOARD_NOT_FORBIDDEN", "게시글을 권한이 없습니다.")
    ;


    private final int httpStatus;
    private final String code;
    private final String message;
}
