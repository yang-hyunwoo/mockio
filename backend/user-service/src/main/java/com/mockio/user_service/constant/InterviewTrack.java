package com.mockio.user_service.constant;

/** 면접 분야 enum
 *  InterviewTrack
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InterviewTrack {
    SOFTWARE_ENGINEER("개발"),
    DATA("데이터 분석가"),
    DESIGN("그래픽"),
    PRODUCT("PM"),
    BUSINESS("전략"),
    MARKETING("마케팅"),
    SALES("영업"),
    HR("HR"),
    GENERAL("범용"),
    ;

    private final String label;
}
