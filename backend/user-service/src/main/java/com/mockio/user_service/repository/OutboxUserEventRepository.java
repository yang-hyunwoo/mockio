package com.mockio.user_service.repository;


import com.mockio.user_service.domain.OutboxUserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxUserEventRepository extends JpaRepository<OutboxUserEvent, Long> {

    @Query("""
           select e from OutboxUserEvent e
           where e.status = 'PENDING'
           order by e.createdAt asc
           """)
    List<OutboxUserEvent> findTop100Pending();
}
