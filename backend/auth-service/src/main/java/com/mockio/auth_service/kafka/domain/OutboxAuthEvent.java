package com.mockio.auth_service.kafka.domain;

/**
 * Auth Service에서 사용하는 Outbox 이벤트 엔티티.
 *
 * <p>외부 인증 시스템(Keycloak 등)과의 연동 작업을
 * 트랜잭션 경계 밖에서 안정적으로 처리하기 위해
 * Outbox 패턴을 적용한 이벤트 저장소 역할을 한다.</p>
 *
 * <p>이 엔티티는 이벤트의 상태 전이(PENDING → PROCESSING → SENT/FAILED/DEAD),
 * 재시도 횟수 관리, 멱등성 보장을 위한 식별자(idempotencyKey),
 * 그리고 장애 복구를 위한 오류 정보(lastError)를 함께 관리한다.</p>
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.common_spring.constant.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outbox_auth_events")
@Entity
public class OutboxAuthEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "aggregate_id", nullable = false, length = 100)
    private String aggregateId;

    /**
     * 동일 이벤트 중복 처리를 방지하기 위한 멱등성 키.
     */
    @Column(name = "idempotency_key", nullable = false, length = 150)
    private String idempotencyKey;

    /**
     * 외부 시스템으로 전달될 이벤트 페이로드(JSON).
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private JsonNode payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OutboxStatus status;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "max_attempts", nullable = false)
    private int maxAttempts;

    @Column(name = "next_attempt_at", nullable = false)
    private OffsetDateTime nextAttemptAt;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    @Column(name = "locked_at")
    private OffsetDateTime lockedAt;

    @Column(name = "locked_by", length = 100)
    private String lockedBy;

    @Column(name = "succeeded_at")
    private OffsetDateTime succeededAt;

    @Builder
    private OutboxAuthEvent(
            String eventType,
            String aggregateId,
            String idempotencyKey,
            JsonNode payload,
            OutboxStatus status,
            int maxAttempts,
            OffsetDateTime nextAttemptAt
    ) {
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.idempotencyKey = idempotencyKey;
        this.payload = payload;
        this.status = status;
        this.maxAttempts = maxAttempts;
        this.nextAttemptAt = nextAttemptAt;
        this.attemptCount = 0;
    }

    public void lock(String lockerId) {
        this.status = OutboxStatus.PROCESSING;
        this.lockedAt = OffsetDateTime.now();
        this.lockedBy = lockerId;
    }

    public void markSucceeded() {
        this.status = OutboxStatus.SENT;
        this.lastError = null;
        this.lockedAt = null;
        this.lockedBy = null;

        OffsetDateTime now = OffsetDateTime.now();
        this.succeededAt = now;
    }

    public void markFailed(String error, OffsetDateTime nextAttemptAt) {
        this.status = OutboxStatus.FAILED;
        this.attemptCount += 1;
        this.lastError = truncate(error, 4000);
        this.nextAttemptAt = nextAttemptAt;
        this.lockedAt = null;
        this.lockedBy = null;
    }

    public void markDead(String error) {
        this.status = OutboxStatus.DEAD;
        this.attemptCount += 1;
        this.lastError = truncate(error, 4000);
        this.lockedAt = null;
        this.lockedBy = null;
    }

    public boolean isExhaustedAfterFail() {
        // 실패 처리 시 attemptCount가 +1 되므로, 그 이후 기준으로 판단하려면 호출 타이밍에 주의
        return (this.attemptCount + 1) >= this.maxAttempts;
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}