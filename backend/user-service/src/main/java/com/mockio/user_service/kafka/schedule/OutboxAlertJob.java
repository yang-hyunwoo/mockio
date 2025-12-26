package com.mockio.user_service.kafka.schedule;

import com.mockio.user_service.constant.OutboxStatus;
import com.mockio.user_service.kafka.repository.OutboxUserEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxAlertJob {

    private final OutboxUserEventRepository repo;

    @Scheduled(fixedDelay = 60000) // 1분
    public void alertIfDeadExists() {
        long dead = repo.countByStatus(OutboxStatus.DEAD);
        if (dead > 0) {
            // 여기서 Slack/Webhook 호출로 확장 가능
            log.error("Outbox DEAD events detected. count={}", dead);
        }
    }
}
