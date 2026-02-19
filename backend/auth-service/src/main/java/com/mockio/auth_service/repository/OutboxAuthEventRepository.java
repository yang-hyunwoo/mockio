package com.mockio.auth_service.repository;

/**
 * Auth Service Outbox 이벤트를 관리하는 JPA Repository.
 *
 * <p>Outbox 패턴 구현을 위해 이벤트 조회, 락 획득,
 * 처리 상태 전이(PROCESSING / SENT / FAILED / DEAD)를
 * 전용 쿼리로 수행한다.</p>
 *
 * <p>분산 워커 환경에서 중복 처리를 방지하기 위해
 * SELECT FOR UPDATE SKIP LOCKED 전략을 사용한다.</p>
 */

import com.mockio.auth_service.kafka.domain.OutboxAuthEvent;
import com.mockio.common_spring.constant.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface OutboxAuthEventRepository extends JpaRepository<OutboxAuthEvent, Long> {

    /**
     * 멱등성 키로 Outbox 이벤트를 조회한다.
     *
     * @param idempotencyKey 멱등성 키
     * @return 조회된 Outbox 이벤트 (없으면 empty)
     */
    Optional<OutboxAuthEvent> findByIdempotencyKey(String idempotencyKey);

    /**
     * 처리 기한이 도래한 Outbox 이벤트를 락과 함께 조회한다.
     *
     * <p>NEW / PENDING / FAILED 상태 중
     * nextAttemptAt이 현재 시각 이전인 이벤트를 대상으로 하며,
     * FOR UPDATE SKIP LOCKED를 사용하여
     * 워커 간 중복 처리를 방지한다.</p>
     *
     * @param limit 한 번에 조회할 최대 이벤트 수
     * @return 락이 획득된 Outbox 이벤트 목록
     */
    @Query(value = """
            SELECT *
            FROM outbox_auth_events
            WHERE status IN ('NEW','PENDING','FAILED') 
              AND next_attempt_at <= now()
            ORDER BY next_attempt_at ASC, created_at ASC
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxAuthEvent> lockTopDue(@Param("limit") int limit);

    /**
     * Outbox 이벤트를 재시도(FAILED) 또는 DEAD 상태로 갱신한다.
     *
     * <p>실패 횟수 증가, 다음 재시도 시각 설정,
     * 오류 메시지 기록 및 락 해제를 함께 수행한다.</p>
     *
     * @param id Outbox 이벤트 ID
     * @param status 변경할 상태(FAILED 또는 DEAD)
     * @param attemptCount 갱신될 시도 횟수
     * @param nextAttemptAt 다음 재시도 시각
     * @param lastError 실패 원인 메시지
     * @param now 갱신 시각
     * @return 갱신된 행 수
     */
    @Modifying
    @Query("""
        update OutboxAuthEvent e
           set e.status = :status,
               e.attemptCount = :attemptCount,
               e.nextAttemptAt = :nextAttemptAt,
               e.lastError = :lastError,
               e.lockedAt = null,
               e.lockedBy = null,
               e.updatedAt = :now
         where e.id = :id
    """)
    int updateForRetryOrDead(@Param("id") Long id,
                             @Param("status") OutboxStatus status,
                             @Param("attemptCount") int attemptCount,
                             @Param("nextAttemptAt") OffsetDateTime nextAttemptAt,
                             @Param("lastError") String lastError,
                             @Param("now") OffsetDateTime now);

    /**
     * Outbox 이벤트를 성공(SENT) 상태로 갱신한다.
     *
     * <p>성공 시각을 기록하고,
     * 오류 정보 및 락 정보를 초기화한다.</p>
     *
     * @param id Outbox 이벤트 ID
     * @param now 처리 완료 시각
     * @return 갱신된 행 수
     */
    @Modifying
    @Query("""
        update OutboxAuthEvent e
           set e.status = 'SENT',
               e.lastError = null,
               e.lockedAt = null,
               e.lockedBy = null,
               e.updatedAt = :now,
               e.succeededAt = :now
         where e.id = :id
    """)
    int markSucceeded(@Param("id") Long id,
                      @Param("now") OffsetDateTime now);

    /**
     * Outbox 이벤트를 처리 중(PROCESSING) 상태로 전환한다.
     *
     * <p>락 획득 시각과 처리 워커 식별자를 함께 기록하여,
     * 동시 처리 경쟁을 방지한다.</p>
     *
     * @param id Outbox 이벤트 ID
     * @param lockedBy 처리 워커 식별자
     * @param now 락 획득 시각
     * @return 갱신된 행 수
     */
    @Modifying
    @Query("""
        update OutboxAuthEvent e
           set e.status = 'PROCESSING',
               e.lockedAt = :now,
               e.lockedBy = :lockedBy,
               e.updatedAt = :now
         where e.id = :id
    """)
    int markProcessing(@Param("id") Long id,
                       @Param("lockedBy") String lockedBy,
                       @Param("now") OffsetDateTime now);

    /**
     * 멱등성 키 존재 여부를 확인한다.
     *
     * <p>Outbox 이벤트 중복 적재 방지를 위해 사용된다.</p>
     *
     * @param IdempotencyKey 멱등성 키
     * @return 존재하면 true
     */
    boolean existsByIdempotencyKey(String IdempotencyKey);

}
