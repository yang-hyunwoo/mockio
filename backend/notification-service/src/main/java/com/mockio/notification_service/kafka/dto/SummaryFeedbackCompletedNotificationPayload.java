package com.mockio.notification_service.kafka.dto;

public record SummaryFeedbackCompletedNotificationPayload(
        Long userId,
        Long interviewId,
        Long summaryFeedbackId,
        Integer totalScore,
        String notificationType
) {
}
