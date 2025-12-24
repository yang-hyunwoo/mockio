package com.mockio.user_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "outbox_user_events")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OutboxUserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;

    @Column(name = "aggregate_type", nullable = false, length = 50)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // PENDING/SENT/FAILED

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    public static OutboxUserEvent pending(UUID eventId,
                                          Long userId,
                                          String eventType,
                                          String payloadJson) {
        OutboxUserEvent e = new OutboxUserEvent();
        e.eventId = eventId;
        e.aggregateType = "USER";
        e.aggregateId = userId;
        e.eventType = eventType;
        e.payload = payloadJson;
        e.status = "PENDING";
        e.createdAt = OffsetDateTime.now();
        return e;
    }

    public void markSent() {
        this.status = "SENT";
        this.sentAt = OffsetDateTime.now();
    }

    public void markFailed() {
        this.status = "FAILED";
    }
}
