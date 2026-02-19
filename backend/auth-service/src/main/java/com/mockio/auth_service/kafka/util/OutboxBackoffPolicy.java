package com.mockio.auth_service.kafka.util;

/**
 * Outbox 이벤트 재시도를 위한 백오프(backoff) 정책 유틸리티.
 *
 * <p>이벤트 처리 실패 시 시도 횟수에 따라
 * 다음 재시도까지의 지연 시간을 단계적으로 증가시킨다.</p>
 *
 * <p>외부 시스템 장애가 장기화될 경우
 * 과도한 재시도로 인한 부하를 방지하기 위한 목적이다.</p>
 */

import java.time.Duration;

public class OutboxBackoffPolicy {

    /**
     * 다음 재시도까지의 지연 시간을 계산한다.
     *
     * <p>시도 횟수가 증가할수록 지연 시간을 점진적으로 늘리는
     * 계단형(step-based) 백오프 정책을 적용한다.</p>
     *
     * @param nextAttemptCount 다음 시도 횟수 (1부터 시작)
     * @return 다음 재시도까지의 지연 시간
     */
    public static Duration nextDelay(int nextAttemptCount) {
        return switch (nextAttemptCount) {
            case 1 -> Duration.ofMinutes(1);
            case 2 -> Duration.ofMinutes(5);
            case 3 -> Duration.ofMinutes(30);
            case 4 -> Duration.ofHours(2);
            case 5 -> Duration.ofHours(6);
            case 6 -> Duration.ofHours(12);
            default -> Duration.ofHours(24);
        };
    }

}
