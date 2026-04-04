package com.mockio.support_service.notification.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.support_service.notification.dto.request.NotificationListResponseDto;
import com.mockio.support_service.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알림",
        description = """
                알림 관련 API입니다.
                
                - 알림 리스트 조회
                - 알림 읽기
                - 알림 전체 읽기
                """
)
@RestController
@RequestMapping("api/notification/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MessageUtil messageUtil;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "알림 리스트 조회")
    @GetMapping("/main/list")
    public ResponseEntity<Response<NotificationListResponseDto>> getNotificationList(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), notificationService.getNotificationList(userId));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "알림 읽기")
    @PatchMapping("/main/{notificationId}/read")
    public ResponseEntity<Response<Void>> readNotification(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "알림ID", example = "1") Long notificationId
    ) {
        notificationService.readNotification(notificationId, userId);
        return Response.update(messageUtil.getMessage("response.read"));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "알림 전체 읽기")
    @PatchMapping("/main/read-all")
    public ResponseEntity<Response<Void>> readAllNotifications(
            @CurrentSubject @Parameter(description = "사용자ID", example = "1") Long userId
    ) {
        notificationService.readAllNotifications(userId);
        return Response.update(messageUtil.getMessage("response.read"));
    }

}
