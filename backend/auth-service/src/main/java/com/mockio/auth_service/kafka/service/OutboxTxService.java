package com.mockio.auth_service.kafka.service;

/**
 * Outbox 이벤트 조회 및 처리 상태 전이를 담당하는 트랜잭션 서비스.
 *
 * <p>처리 기한이 도래한 Outbox 이벤트를 조회하여
 * 동일 트랜잭션 내에서 PROCESSING 상태로 마킹한 뒤,
 * 외부 호출 단계에서 사용할 Task DTO로 변환하여 반환한다.</p>
 *
 * <p>이 서비스는 워커 간 동시 실행 환경에서
 * 중복 처리를 방지하기 위한 락/상태 전이의 단일 진입점 역할을 한다.</p>
 */

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

    /**
     * 처리 기한이 도래한 Outbox 이벤트를 조회하고 PROCESSING 상태로 마킹한다.
     *
     * <p>처리 흐름:
     * <ol>
     *   <li>due 상태의 Outbox 이벤트를 지정된 개수만큼 조회</li>
     *   <li>같은 트랜잭션 내에서 PROCESSING 상태로 전이 및 워커 식별자 기록</li>
     *   <li>외부 호출 단계에서 사용할 Task DTO로 변환하여 반환</li>
     * </ol>
     *
     * <p>이 메서드는 반드시 트랜잭션 내에서 실행되며,
     * 반환된 Task는 트랜잭션 외부에서 처리되어야 한다.</p>
     *
     * @param limit 한 번에 처리할 최대 이벤트 수
     * @param workerId 이벤트를 처리하는 워커 식별자
     * @return PROCESSING으로 전이된 Outbox 이벤트 Task 목록
     */
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
