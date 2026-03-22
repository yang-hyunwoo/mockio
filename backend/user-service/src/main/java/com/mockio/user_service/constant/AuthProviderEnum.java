package com.mockio.user_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthProviderEnum {
    NORMAL("일반"),
    NAVER("네이버"),
    GOOGLE("구글"),
    KAKAO("카카오")
    ;

    private final String label;
}
