package com.mockio.feedback_service.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.feedback_service.kafka.domain.OutboxFeedbackEvent;
import com.mockio.feedback_service.kafka.dto.FeedbackLifecycleEvent;
import com.mockio.feedback_service.kafka.repository.OutboxFeedbackEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxFeedbackEventPublishWorker {

    private final OutboxFeedbackEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "feedback.lifecycle";
    private static final int MAX_ATTEMPTS = 10;
    private static final long MAX_BACKOFF_SECONDS = 300; // 5분

    // (1) 락 걸고 가져오기: 짧게 끝내는 트랜잭션
    @Transactional
    public List<Long> lockPendingIds(String lockerId, int limit) {
        List<OutboxFeedbackEvent> events = outboxRepository.lockTopDue(limit);

        for (OutboxFeedbackEvent e : events) {
            e.markProcessing(lockerId);
        }

        return events.stream()
                .map(OutboxFeedbackEvent::getId)
                .toList();
    }

    // (2) 건별 발행: 각각 독립 커밋
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishOne(String lockerId, Long id) {
        OutboxFeedbackEvent e = outboxRepository.findById(id).orElse(null);
        if (e == null) return;
        log.info("status={}, nextAttemptAt={}", e.getStatus(), e.getNextAttemptAt());

        // 방어 로직: 상태/시간 체크
        if (!e.isPublishableBy(lockerId, OffsetDateTime.now())) return;

        try {
            var envelope = new FeedbackLifecycleEvent(
                    e.getEventId(),
                    e.getAggregateType(),
                    e.getAggregateId(),
                    e.getEventType(),
                    e.getPayload(), // jsonb String → JsonNode
                    OffsetDateTime.now()
            );

            String json = objectMapper.writeValueAsString(envelope);

            kafkaTemplate.send(TOPIC, String.valueOf(e.getAggregateId()), json).get(3, TimeUnit.SECONDS);

            e.markSent();

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            e.markFailed(ie, MAX_ATTEMPTS, MAX_BACKOFF_SECONDS);

        } catch (Exception ex) {
            e.markFailed(ex, MAX_ATTEMPTS, MAX_BACKOFF_SECONDS);
        }
    }

    /**
     * wworkerId
     * @return
     */
    public String workerId() {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            host = "unknown-host";
        }
        long pid = ProcessHandle.current().pid();
        return host + "-" + pid;
    }

}
