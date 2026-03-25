package com.mockio.core_service.feedback.kafka.dto.request;

public record SummaryFeedbackCompletedNotificationPayload(
        Long userId,
        Long interviewId,
        Long summaryFeedbackId,
        Integer totalScore,
        String notificationType
) {}
