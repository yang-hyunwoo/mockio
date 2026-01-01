package com.mockio.user_service.kafka.repository;


import com.mockio.common_spring.constant.OutboxStatus;
import com.mockio.user_service.kafka.domain.OutboxUserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface OutboxUserEventRepository extends JpaRepository<OutboxUserEvent, Long> {

    @Query(value = """
    SELECT *
    FROM outbox_user_events
    WHERE status IN ('NEW','PENDING','FAILED') 
      AND next_attempt_at <= now()
    ORDER BY next_attempt_at ASC, created_at ASC
    LIMIT :limit
    FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<OutboxUserEvent> lockTopDue(@Param("limit") int limit);

    @Query("""
        select count(e)
        from OutboxUserEvent e
        where e.status = :status
    """)
    long countByStatus(@Param("status") OutboxStatus status);

    @Query("""
        select count(e)
        from OutboxUserEvent e
        where e.status in :statuses
          and e.nextAttemptAt <= :now
    """)
    long countDue(@Param("statuses") List<OutboxStatus> statuses,
                  @Param("now") OffsetDateTime now);

    List<OutboxUserEvent> findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus status);

    @Modifying
    @Query("""
        update OutboxUserEvent e
           set e.status = :status,
               e.attemptCount = :attemptCount,
               e.nextAttemptAt = :nextAttemptAt,
               e.lastError = null,
               e.lockedAt = null,
               e.lockedBy = null
         where e.id = :id
    """)
    int resetForRetry(@Param("id") Long id,
                      @Param("status") OutboxStatus status,
                      @Param("attemptCount") int attemptCount,
                      @Param("nextAttemptAt") OffsetDateTime nextAttemptAt);

}
