package com.mockio.support_service.notification.dto.request;

import com.mockio.common_spring.util.response.EnumResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

public record NotificationListResponseDto(

        @Schema(description = "읽지 않은 갯수", example = "1")
        int unreadCount,

        @Schema(description = "다음 여부", example = "true")
        boolean hasNext,

        @Schema(description = "알림 리스트", example = "[]")
        List<Item> notifications

) {
    public record Item(

            @Schema(description = "ID", example = "1")
            Long id,

            @Schema(description = "알림 타입", example = "notice")
            EnumResponse type,

            @Schema(description = "제목", example = "제목")
            String title,

            @Schema(description = "내용", example = "내용")
            String content,

            @Schema(description = "링크", example = "/asdf/asdf")
            String link,

            @Schema(description = "읽음 여부", example = "true")
            boolean isRead,

            @Schema(description = "생성일", example = "2020-01-01 22:22:222")
            OffsetDateTime createdAt

    ) {}
}
