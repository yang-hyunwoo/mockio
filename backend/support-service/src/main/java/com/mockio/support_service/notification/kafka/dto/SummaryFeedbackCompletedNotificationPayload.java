package com.mockio.support_service.notification.kafka.dto;

public record SummaryFeedbackCompletedNotificationPayload(
        Long userId,
        Long interviewId,
        Long summaryFeedbackId,
        Integer totalScore,
        String notificationType
) {}
