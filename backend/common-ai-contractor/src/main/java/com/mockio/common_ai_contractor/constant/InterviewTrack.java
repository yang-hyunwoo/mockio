package com.mockio.common_ai_contractor.constant;

/**
 * 면접 분야 enum
 */

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum InterviewTrack {
    BACKEND_ENGINEER("백엔드 엔지니어"),
    FRONTEND_ENGINEER("프론트엔드 엔지니어"),
    SERVER_ENGINEER("서버 엔지니어"),
    DATA_ENGINEER("데이터 분석가"),
    DESIGN("그래픽 디자이너"),
    PRODUCT("PM"),
    BUSINESS("전략"),
    MARKETING("마케팅"),
    SALES("영업"),
    HR("HR"),
    GENERAL("범용"),
    ;

    private final String label;

}
