package com.mockio.user_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OutboxStatus {
    PENDING("대기"),
    PROCESSING("실행중"),
    SENT("전송"),
    DEAD("실패");

    private final String label;
}
