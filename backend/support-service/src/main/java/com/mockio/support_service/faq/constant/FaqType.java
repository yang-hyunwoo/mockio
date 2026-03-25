package com.mockio.support_service.faq.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FaqType {
    ACCOUNT("계정"),
    INTERVIEW("면접 이용"),
    FEEDBACK("AI 피드백"),
    PAYMENT("결제"),
    TECHNICAL("기술 문제"),
    ETC("기타");

    private final String label;

}
