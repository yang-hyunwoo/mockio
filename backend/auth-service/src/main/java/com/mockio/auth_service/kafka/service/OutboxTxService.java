package com.mockio.auth_service.kafka.service;

import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.auth_service.kafka.dto.OutboxAuthEventTask;
import com.mockio.auth_service.repository.OutboxAuthEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxTxService {

    private final OutboxAuthEventRepository outboxRepo;

    @Transactional
    public List<OutboxAuthEventTask> fetchAndMarkProcessing(int limit, String workerId) {
        List<OutboxAuthEvent> due = outboxRepo.lockTopDue(limit);
        if (due.isEmpty()) return List.of();

        OffsetDateTime now = OffsetDateTime.now();

        // 1) PROCESSING 마킹
        for (OutboxAuthEvent e : due) {
            outboxRepo.markProcessing(e.getId(), workerId, now);
        }

        // 2) 트랜잭션 밖으로 전달할 Task DTO로 변환
        return due.stream()
                .map(e -> new OutboxAuthEventTask(
                        e.getId(),
                        e.getAggregateId(),
                        e.getPayload(),
                        e.getAttemptCount(),
                        e.getMaxAttempts()
                ))
                .toList();
    }
}
