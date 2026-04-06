package com.mockio.core_service.user.kafka.repository;

import com.mockio.core_service.user.kafka.domain.OutboxUserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
