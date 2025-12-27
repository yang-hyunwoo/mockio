package com.mockio.user_service.kafka.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.user_service.PostgresDataJpaTest;
import com.mockio.user_service.kafka.domain.OutboxUserEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
class OutboxUserEventRepositoryTest extends PostgresDataJpaTest {

    @Autowired
    OutboxUserEventRepository repo;

    @Autowired
    PlatformTransactionManager txManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void seed() {
        // 1) 기존 데이터 정리도 커밋되도록 REQUIRES_NEW로
        TransactionTemplate seedTx = new TransactionTemplate(txManager);
        seedTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        seedTx.execute(status -> {
            repo.deleteAll();

            for (int i = 0; i < 150; i++) {
                OutboxUserEvent e = OutboxUserEvent.pending(
                        UUID.randomUUID(),
                        (long) (1000 + i),
                        "USER_DELETED",
                        objectMapper.valueToTree(Map.of("idx", i))
                );
                repo.save(e);
            }
            em.flush();
            return null;
        });

        em.clear();
    }

    @Test
    void lockTop100Due_shouldSkipLockedRows_betweenConcurrentTransactions() throws Exception {
        TransactionTemplate tx1 = new TransactionTemplate(txManager);
        TransactionTemplate tx2 = new TransactionTemplate(txManager);

        CountDownLatch tx1Locked = new CountDownLatch(1);
        CountDownLatch allowTx1Commit = new CountDownLatch(1);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<Set<Long>> f1 = pool.submit(() ->
                tx1.execute(status -> {
                    List<OutboxUserEvent> locked = repo.lockTop100Due();
                    Set<Long> ids = locked.stream().map(OutboxUserEvent::getId).collect(Collectors.toSet());

                    // tx1이 row-lock을 잡은 시점
                    tx1Locked.countDown();

                    // 락 유지 (tx2가 조회할 시간을 줌)
                    try {
                        allowTx1Commit.await(3, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return ids;
                })
        );

        Future<Set<Long>> f2 = pool.submit(() -> {
            // tx1이 먼저 락을 잡도록 대기
            tx1Locked.await(3, TimeUnit.SECONDS);

            return tx2.execute(status -> {
                List<OutboxUserEvent> locked = repo.lockTop100Due();
                return locked.stream().map(OutboxUserEvent::getId).collect(Collectors.toSet());
            });
        });

        Set<Long> ids1 = f1.get(5, TimeUnit.SECONDS);
        Set<Long> ids2 = f2.get(5, TimeUnit.SECONDS);

        // tx1 커밋 허용
        allowTx1Commit.countDown();

        pool.shutdownNow();

        // then: 교집합이 없어야 함 (SKIP LOCKED 검증)
        Set<Long> intersection = new HashSet<>(ids1);
        intersection.retainAll(ids2);

        assertThat(intersection).isEmpty();
        assertThat(ids1).hasSizeLessThanOrEqualTo(100);
        assertThat(ids2).hasSizeLessThanOrEqualTo(100);
        assertThat(ids1).isNotEmpty();
        assertThat(ids2).isNotEmpty();
    }
}
