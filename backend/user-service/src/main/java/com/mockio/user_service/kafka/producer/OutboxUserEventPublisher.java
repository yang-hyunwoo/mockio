package com.mockio.user_service.kafka.producer;

import com.mockio.user_service.kafka.repository.OutboxUserEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class OutboxUserEventPublisher {

    private final OutboxUserEventRepository outboxRepository;
    private final OutboxUserEventPublishWorker worker;

    @Scheduled(fixedDelay = 1000)
    public void publishPending() {
        // 1) 락 걸고 가져오는 작업은 worker 쪽에서 트랜잭션으로 처리
        List<Long> ids = worker.lockPendingIds(100);
        if (ids.isEmpty()) return;

        // 2) 건별 발행은 REQUIRES_NEW로 처리
        for (Long id : ids) {
            worker.publishOne(id);
        }
    }
}
