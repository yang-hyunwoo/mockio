package com.mockio.interview_service.kafka.repository;

import com.mockio.interview_service.kafka.domain.OutboxInterviewEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxInterviewEventRepository extends JpaRepository<OutboxInterviewEvent , Long> {

    @Query(value = """
    SELECT *
    FROM outbox_interview_events
    WHERE status IN ('NEW','PENDING','FAILED') 
      AND next_attempt_at <= now()
    ORDER BY next_attempt_at ASC, created_at ASC
    LIMIT :limit
    FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<OutboxInterviewEvent> lockTopDue(@Param("limit") int limit);

}
