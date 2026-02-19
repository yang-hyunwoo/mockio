package com.mockio.common_spring.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    PENDING("대기"),
    PROCESS("진행중"),
    SUCCEEDED("성공"),
    FAILED("실패"),
    RETRY("재시도")
    ;

    private final String label;

}
