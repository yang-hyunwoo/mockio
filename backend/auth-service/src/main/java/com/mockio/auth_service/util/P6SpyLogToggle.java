package com.mockio.auth_service.util;

/**
 * P6Spy SQL 로그를 일시적으로 비활성화하기 위한 유틸리티 클래스.
 *
 * <p>대량 배치 처리나 Outbox 워커 실행 시
 * 과도한 SQL 로그 출력으로 인한 성능 저하 및 로그 노이즈를
 * 방지하기 위해 사용된다.</p>
 *
 * <p>지정된 작업(action) 수행 전 P6Spy 로그 레벨을 OFF로 전환하고,
 * 작업 종료 후 반드시 이전 로그 레벨로 복구한다.</p>
 */

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public final class P6SpyLogToggle {
    private static final Logger P6SPY = (Logger) LoggerFactory.getLogger("p6spy");

    private P6SpyLogToggle() {}

    /**
     * 지정된 작업을 P6Spy 로그 비활성화 상태에서 실행하고 결과를 반환한다.
     *
     * <p>작업 수행 중 예외가 발생하더라도
     * finally 블록에서 로그 레벨을 원래 상태로 복구한다.</p>
     *
     * @param action 로그 비활성화 상태에서 실행할 작업
     * @param <T> 작업 결과 타입
     * @return 작업 실행 결과
     * @throws RuntimeException 작업 수행 중 예외가 발생한 경우
     */
    public static <T> T withoutP6Spy(java.util.concurrent.Callable<T> action) {
        Level prev = P6SPY.getLevel();
        try {
            P6SPY.setLevel(Level.OFF);
            return action.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            P6SPY.setLevel(prev);
        }
    }

    /**
     * 지정된 작업을 P6Spy 로그 비활성화 상태에서 실행한다.
     *
     * <p>반환값이 없는 작업(Runnable)에 사용되며,
     * 작업 종료 후 로그 레벨은 반드시 원래 상태로 복구된다.</p>
     *
     * @param action 로그 비활성화 상태에서 실행할 작업
     */
    public static void withoutP6Spy(Runnable action) {
        Level prev = P6SPY.getLevel();
        try {
            P6SPY.setLevel(Level.OFF);
            action.run();
        } finally {
            P6SPY.setLevel(prev);
        }
    }
}
