package com.mockio.notification_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.notification_service.constant.NotificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 500)
    private String link;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "source_event_id", nullable = false, unique = true, length = 100)
    private String sourceEventId;

    @Column(name = "reference_type", nullable = false, length = 50)
    private String referenceType;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Builder
    private Notification(Long id,
                         Long userId,
                         NotificationType type,
                         String title,
                         String content,
                         String link,
                         boolean isRead,
                         OffsetDateTime readAt,
                         String sourceEventId,
                         String referenceType,
                         Long referenceId
    ) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.link = link;
        this.isRead = isRead;
        this.readAt = readAt;
        this.sourceEventId = sourceEventId;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
    }

    public static Notification create(
            Long userId,
            NotificationType type,
            String title,
            String content,
            String link,
            String sourceEventId,
            String referenceType,
            Long referenceId
    ) {
        return Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .content(content)
                .link(link)
                .isRead(false)
                .sourceEventId(sourceEventId)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .build();

    }

    public void markAsRead() {
        if (this.isRead) return;

        this.isRead = true;
        this.readAt = OffsetDateTime.now();
    }

}
