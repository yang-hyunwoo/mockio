package com.mockio.user_service.kafka;

import com.mockio.user_service.domain.OutboxUserEvent;
import com.mockio.user_service.repository.OutboxUserEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxUserEventPublisher {

    private final OutboxUserEventRepository outboxRepository;
//    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "user.lifecycle";

    @Scheduled(fixedDelay = 1000) // 1초마다
    @Transactional
    public void publishPending() {
        List<OutboxUserEvent> pending = outboxRepository.findTop100Pending();
        if (pending.isEmpty()) return;

        for (OutboxUserEvent e : pending) {
            try {
                // key는 aggregateId로(동일 user 순서성)
//                kafkaTemplate.send(TOPIC, String.valueOf(e.getAggregateId()), e.getPayload());
                e.markSent();
            } catch (Exception ex) {
                log.error("outbox publish failed. eventId={}", e.getEventId(), ex);
                e.markFailed();
            }
        }
    }
}
