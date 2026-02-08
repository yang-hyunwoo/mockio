package com.mockio.faq_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FaqType {
    ALL("전체"),
    ACCOUNT("계정") ,
    SERVICE("서비스"),
    ;

    private final String label;
}
