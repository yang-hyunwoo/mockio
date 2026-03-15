package com.mockio.notification_service.mapper;

import com.mockio.common_spring.util.response.EnumResponse;
import com.mockio.notification_service.domain.Notification;
import com.mockio.notification_service.dto.request.NotificationListResponseDto;
import com.mockio.notification_service.dto.request.NotificationListResponseDto.Item;

import java.util.List;

public class NotificationMapper {

    public static NotificationListResponseDto from(
            List<Notification> notifications,
            int unreadCount,
            boolean hasNext
    ) {

        List<Item> items =
                notifications.stream()
                        .map(NotificationMapper::from)
                        .toList();

        return new NotificationListResponseDto(
                unreadCount,
                hasNext,
                items
        );
    }

    public static Item from(Notification notification) {

        return new Item(
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
