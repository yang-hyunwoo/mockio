package com.mockio.notification_service.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.notification_service.constant.NotificationErrorCode;
import com.mockio.notification_service.constant.NotificationType;
import com.mockio.notification_service.domain.Notification;
import com.mockio.notification_service.dto.request.NotificationListResponseDto;
import com.mockio.notification_service.kafka.dto.SummaryFeedbackCompletedNotificationPayload;
import com.mockio.notification_service.mapper.NotificationMapper;
import com.mockio.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.mockio.notification_service.constant.NotificationErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;


    @Transactional(readOnly = true)
    public NotificationListResponseDto getNotificationList(Long userId) {
        Pageable pageable = PageRequest.of(0, 10);

        List<Notification> notifications = notificationRepository.findTop10ByUserId(userId, pageable);
        int unreadCount = notificationRepository.countByUserIdAndIsReadFalse(userId);
        boolean hasNext = notifications.size() == 10;

        return NotificationMapper.from(
                notifications,
                unreadCount,
                hasNext
        );
    }

    public void readNotification(Long notificationId , Long userId) {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId,userId)
                .orElseThrow(() -> new CustomApiException(NOTIFICATION_FORBIDDEN.getHttpStatus(),NOTIFICATION_FORBIDDEN, NOTIFICATION_FORBIDDEN.getMessage()));
        notification.markAsRead();
    }

    public void readAllNotifications(Long userId) {
        notificationRepository.readAllByUserId(userId);
    }

    public void createSummaryFeedbackReadyNotification(UUID eventId, SummaryFeedbackCompletedNotificationPayload payload) {
        try {
            Notification notification = Notification.create(
                    payload.userId(),
                    NotificationType.SUMMARY_FEEDBACK_COMPLETED,
                    "면접 종합 피드백이 도착했습니다.",
                    "면접 결과와 종합 피드백을 확인해 보세요. (점수: " + payload.totalScore() + "점)",
                    "/interview/result/" + payload.interviewId(),
                    eventId.toString(),
                    "INTERVIEW",
                    payload.interviewId()
            );

            notificationRepository.save(notification);
        } catch (DataIntegrityViolationException e) {
            notificationRepository
                    .findBySourceEventId(eventId.toString())
                    .orElseGet(() ->
                            notificationRepository
                                    .findByUserIdAndTypeAndReferenceTypeAndReferenceId(
                                            payload.userId(),
                                            NotificationType.SUMMARY_FEEDBACK_COMPLETED,
                                            "INTERVIEW",
                                            payload.interviewId()
                                    )
                                    .orElseThrow(() -> e)
                    );
        }
    }

}
