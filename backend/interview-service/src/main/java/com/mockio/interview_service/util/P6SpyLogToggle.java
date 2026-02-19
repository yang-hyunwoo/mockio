package com.mockio.interview_service.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public final class P6SpyLogToggle {
    private static final Logger P6SPY = (Logger) LoggerFactory.getLogger("p6spy");

    private P6SpyLogToggle() {}

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