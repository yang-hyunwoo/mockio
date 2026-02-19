package com.mockio.interview_service.kafka.producer;

import com.mockio.interview_service.util.P6SpyLogToggle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class OutboxInterviewEventPublisher {
    private final OutboxInterviewEventPublishWorker worker;

    @Scheduled(fixedDelay = 500)
    public void publishPending() {
        P6SpyLogToggle.withoutP6Spy(() -> {
            String lockerId = worker.workerId(); // workerId를 public으로 열거나 별도 메서드로
            List<Long> ids = worker.lockPendingIds(lockerId, 100);
            for (Long id : ids) {
                worker.publishOne(lockerId, id);
            }
        });
    }

}
