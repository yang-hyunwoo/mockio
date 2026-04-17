package com.mockio.core_service.interview.repository;

import com.mockio.common_ai_contractor.constant.InterviewEndReason;
import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.common_ai_contractor.constant.InterviewTrack;
import com.mockio.core_service.interview.domain.Interview;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Optional<Interview> findActiveByUserIdAndStatus(Long userId, InterviewStatus status);

    Optional<Interview> findByUserIdAndIdempotencyKey(Long userId, String idempotencyKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Interview i where i.id = :id and i.userId = :userId")
    Optional<Interview> findByIdAndUserIdForUpdate(@Param("id") Long id, @Param("userId") Long userId);

    Optional<Interview> findByIdAndUserId(Long id, Long userId);

    List<Interview> findByUserIdAndStatusAndEndedAtIsNullOrderByCreatedAt(Long userId , InterviewStatus status);

    @Modifying
    @Query("update Interview i set i.totalCount = i.totalCount + 1 where i.id = :id")
    void incrementTotalCount(@Param("id") Long id);

    @Query("""
            SELECT i
            FROM Interview i
            WHERE i.userId = :userId
              AND i.status <> :status
            ORDER BY
              CASE WHEN i.status = 'ACTIVE' THEN 0 ELSE 1 END,
              i.createdAt DESC
            """)
    Page<Interview> findByUserIdOrderByActiveFirst(
            @Param("userId") Long userId,
            @Param("status") InterviewStatus status,
            Pageable pageable
    );

    boolean existsByUserIdAndStatus(Long userId , InterviewStatus status);

    List<Interview> findByUserIdAndStatusAndEndReason(
            Long userId,
            InterviewStatus status,
            InterviewEndReason endReason,
            Pageable pageable
    );

    List<Interview> findByUserIdAndStatusAndEndReasonAndTrack(
            Long userId,
            InterviewStatus status,
            InterviewEndReason endReason,
            InterviewTrack track,
            Pageable pageable
    );

    Page<Interview> findByUserIdAndStatusOrderByIdDesc(
            Long userId,
            InterviewStatus status,
            Pageable pageable
    );

    Page<Interview> findByUserIdAndStatusAndTrackOrderByIdDesc(
            Long userId,
            InterviewStatus status,
            InterviewTrack track,
            Pageable pageable
    );

    List<Interview> findTop30ByUserIdAndTrackOrderByCreatedAtDesc(Long userId , InterviewTrack track);

    Optional<Interview> findTopByUserIdAndStatusInOrderByCreatedAtDesc(
            Long userId,
            List<InterviewStatus> statuses
    );


    Optional<Interview> findTopByRootInterviewIdAndCreatedAtBeforeAndEndReasonAndStatusOrderByCreatedAtDesc(Long id,
                                                                                                            OffsetDateTime createdAt,
                                                                                                            InterviewEndReason interviewEndReason,
                                                                                                            InterviewStatus interviewStatus);

    List<Interview> findByUserIdAndStatusAndEndReason(
            Long userId,
            InterviewStatus status,
            InterviewEndReason endReason
    );
}
