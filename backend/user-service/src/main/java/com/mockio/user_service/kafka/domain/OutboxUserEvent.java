package com.mockio.user_service.kafka.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.common_spring.constant.OutboxStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "outbox_user_events")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class OutboxUserEvent extends BaseTimeEntity {

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

    // jsonb 컬럼.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private JsonNode payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OutboxStatus status;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "next_attempt_at", nullable = false)
    private OffsetDateTime nextAttemptAt;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    @Column(name = "locked_at")
    private OffsetDateTime lockedAt;

    @Column(name = "locked_by", length = 128)
    private String lockedBy;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    @Builder
    private OutboxUserEvent(
            Long id,
            UUID eventId,
            String aggregateType,
            Long aggregateId,
            String eventType,
            JsonNode payload,
            OutboxStatus status,
            int attemptCount,
            OffsetDateTime nextAttemptAt,
            String lastError,
            OffsetDateTime lockedAt,
            String lockedBy,
            OffsetDateTime sentAt
    ) {
        this.id = id;
        this.eventId = eventId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.attemptCount = attemptCount;
        this.nextAttemptAt = nextAttemptAt;
        this.lastError = lastError;
        this.lockedAt = lockedAt;
        this.lockedBy = lockedBy;
        this.sentAt = sentAt;
    }

    public static OutboxUserEvent createNew(UUID eventId,
                                          Long userId,
                                          String eventType,
                                          JsonNode payloadJson) {
        return OutboxUserEvent.builder()
                .eventId(eventId)
                .aggregateType("USER")
                .aggregateId(userId)
                .eventType(eventType)
                .payload(payloadJson)
                .status(OutboxStatus.NEW)
                .attemptCount(0)
                .nextAttemptAt(OffsetDateTime.now())
                .build();
    }

    public static OutboxUserEvent pending(UUID eventId,
                                          Long userId,
                                          String eventType,
                                          JsonNode payloadJson) {
        return OutboxUserEvent.builder()
                .eventId(eventId)
                .aggregateType("USER")
                .aggregateId(userId)
                .eventType(eventType)
                .payload(payloadJson)
                .status(OutboxStatus.PENDING)
                .attemptCount(0)
                .nextAttemptAt(OffsetDateTime.now())
                .build();
    }

    public boolean isDue(OffsetDateTime now) {
        return !nextAttemptAt.isAfter(now);
    }

    public boolean isReadyToPublish() {
        return status == OutboxStatus.PROCESSING;
    }

    public void markProcessing(String lockerId) {
        if (CAN_MARK_PROCESSING.contains(this.status)) {
            this.status = OutboxStatus.PROCESSING;
            this.lockedAt = OffsetDateTime.now();
            this.lockedBy = lockerId;
        }
    }

    public void markSent() {
        this.status = OutboxStatus.SENT;
        this.sentAt = OffsetDateTime.now();
        this.lastError = null;
        this.lockedAt = null;
        this.lockedBy = null;
    }

    /**
     * @param ex 실패 원인
     * @param maxAttempts 예: 10
     * @param maxBackoffSeconds 예: 300 (5분)
     */
    public void markFailed(Throwable ex, int maxAttempts, long maxBackoffSeconds) {
        this.attemptCount++;

        this.lastError = truncate(ex == null ? "unknown" : ex.getMessage(), 2000);

        if (this.attemptCount >= maxAttempts) {
            this.status = OutboxStatus.DEAD;
            // DEAD면 다음 시도 스케줄 의미 없으므로 고정해도 되고 null로 두고 싶으면 DDL을 nullable로 바꿔야 함
            return;
        }

        long delaySeconds = (long) Math.pow(2, this.attemptCount); // 2^n
        if (delaySeconds > maxBackoffSeconds) delaySeconds = maxBackoffSeconds;

        this.nextAttemptAt = OffsetDateTime.now().plusSeconds(delaySeconds);
        this.status = OutboxStatus.PENDING;

        this.lockedAt = null;
        this.lockedBy = null;
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return null;
        return s.length() <= maxLen ? s : s.substring(0, maxLen);
    }

    private static final EnumSet<OutboxStatus> CAN_MARK_PROCESSING = EnumSet.of(OutboxStatus.NEW, OutboxStatus.FAILED);
}