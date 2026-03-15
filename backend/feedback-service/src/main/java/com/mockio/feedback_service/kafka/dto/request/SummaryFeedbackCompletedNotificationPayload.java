package com.mockio.feedback_service.kafka.dto.request;

public record SummaryFeedbackCompletedNotificationPayload(
        Long userId,
        Long interviewId,
        Long summaryFeedbackId,
        Integer totalScore,
        String notificationType
) {
}
