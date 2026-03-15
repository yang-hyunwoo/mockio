package com.mockio.notification_service.dto.request;

import com.mockio.common_spring.util.response.EnumResponse;

import java.time.OffsetDateTime;
import java.util.List;

public record NotificationListResponseDto(
        int unreadCount,
        boolean hasNext,
        List<Item> notifications
) {
    public record Item(
            Long id,
            EnumResponse type,
            String title,
            String content,
            String link,
            boolean isRead,
            OffsetDateTime createdAt
    ) {
    }
}
