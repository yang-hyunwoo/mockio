package com.mockio.notification_service.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationType {
    INTERVIEW_COMPLETED("면접 생성 알림"),
    SUMMARY_FEEDBACK_COMPLETED("면접 요약 피드백 완료 알림"),
    SYSTEM_NOTICE("시스템 알림"),
    ;

    private final String label;
}
