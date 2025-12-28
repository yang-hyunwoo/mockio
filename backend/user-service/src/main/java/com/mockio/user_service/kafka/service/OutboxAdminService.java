package com.mockio.user_service.kafka.service;

import com.mockio.common_spring.constant.OutboxStatus;
import com.mockio.user_service.kafka.domain.OutboxUserEvent;
import com.mockio.user_service.kafka.repository.OutboxUserEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxAdminService {

    private final OutboxUserEventRepository repo;

    @Transactional
    public int retryDeadEvents(int max) {
        List<OutboxUserEvent> dead = repo.findTop50ByStatusOrderByCreatedAtAsc(OutboxStatus.DEAD);
        int count = 0;

        for (OutboxUserEvent e : dead) {
            if (count >= max) break;

            int updated = repo.resetForRetry(
                    e.getId(),
                    OutboxStatus.PENDING,
                    0,
                    OffsetDateTime.now()
            );

            count += updated;
        }

        return count;
    }
}