package com.mockio.support_service.notification.mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.support_service.notification.domain.Notification;
import com.mockio.support_service.notification.dto.request.NotificationListResponseDto;

import java.util.List;

public class NotificationMapper {

    public static NotificationListResponseDto from(
            List<Notification> notifications,
            int unreadCount,
            boolean hasNext
    ) {
        List<NotificationListResponseDto.Item> items =
                notifications.stream()
                        .map(NotificationMapper::from)
                        .toList();

        return new NotificationListResponseDto(
                unreadCount,
                hasNext,
                items
        );
    }

    public static NotificationListResponseDto.Item from(Notification notification) {
        return new NotificationListResponseDto.Item(
                notification.getId(),
                EnumResponse.of(
                        notification.getType().name(),
                        notification.getType().getLabel()
                ),
                notification.getTitle(),
                notification.getContent(),
                notification.getLink(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

}
