package com.mockio.common_spring.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OutboxStatus  {
    NEW("발행 대기"),
    PENDING("대기"),
    PROCESSING("실행중"),
    SENT("완료"),
    FAILED("실패(재시도 가능)"),
    DEAD("실패");

    private final String label;

}
