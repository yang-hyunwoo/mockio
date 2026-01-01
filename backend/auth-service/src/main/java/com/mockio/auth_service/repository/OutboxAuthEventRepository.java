package com.mockio.auth_service.repository;

import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.common_spring.constant.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxAuthEventRepository extends JpaRepository<OutboxAuthEvent, Long> {

    Optional<OutboxAuthEvent> findByIdempotencyKey(String idempotencyKey);

    @Query(value = """
    SELECT *
    FROM outbox_auth_events
    WHERE status IN ('NEW','PENDING','FAILED') 
      AND next_attempt_at <= now()
    ORDER BY next_attempt_at ASC, created_at ASC
    LIMIT :limit
    FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<OutboxAuthEvent> lockTopDue(@Param("limit") int limit);

    @Modifying
    @Query("""
        update OutboxAuthEvent e
           set e.status = :status,
               e.attemptCount = :attemptCount,
               e.nextAttemptAt = :nextAttemptAt,
               e.lastError = :lastError,
               e.lockedAt = null,
               e.lockedBy = null,
               e.updatedAt = :now
         where e.id = :id
    """)
    int updateForRetryOrDead(@Param("id") Long id,
                             @Param("status") OutboxStatus status,
                             @Param("attemptCount") int attemptCount,
                             @Param("nextAttemptAt") OffsetDateTime nextAttemptAt,
                             @Param("lastError") String lastError,
                             @Param("now") OffsetDateTime now);

    @Modifying
    @Query("""
        update OutboxAuthEvent e
           set e.status = 'SENT',
               e.lastError = null,
               e.lockedAt = null,
               e.lockedBy = null,
               e.updatedAt = :now,
               e.succeededAt = :now
         where e.id = :id
    """)
    int markSucceeded(@Param("id") Long id,
                      @Param("now") OffsetDateTime now);

    @Modifying
    @Query("""
        update OutboxAuthEvent e
           set e.status = 'PROCESSING',
               e.lockedAt = :now,
               e.lockedBy = :lockedBy,
               e.updatedAt = :now
         where e.id = :id
    """)
    int markProcessing(@Param("id") Long id,
                       @Param("lockedBy") String lockedBy,
                       @Param("now") OffsetDateTime now);

    boolean existsByIdempotencyKey(String IdempotencyKey);
}
