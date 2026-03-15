package com.mockio.notification_service.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.notification_service.dto.request.NotificationListResponseDto;
import com.mockio.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notification/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MessageUtil messageUtil;

    @GetMapping("/main/list")
    public ResponseEntity<Response<NotificationListResponseDto>> getNotificationList(@CurrentSubject Long userId,@AuthenticationPrincipal Jwt jwt) {
        return Response.ok(messageUtil.getMessage("response.read"), notificationService.getNotificationList(userId));
    }

    @PatchMapping("/main/{notificationId}/read")
    public ResponseEntity<Response<Void>> readNotification(@CurrentSubject Long userId,
                                                           @PathVariable Long notificationId) {
        notificationService.readNotification(notificationId,userId);
        return Response.update(messageUtil.getMessage("response.read"));
    }

    @PatchMapping("/main/read-all")
    public ResponseEntity<Response<Void>> readAllNotifications(@CurrentSubject Long userId) {
        notificationService.readAllNotifications(userId);
        return Response.update(messageUtil.getMessage("response.read"));
    }

}
