package com.mockio.interview_service.repository;

import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.interview_service.domain.Interview;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Optional<Interview> findActiveByUserIdAndStatus(String userId, InterviewStatus status);

    int countByUserIdAndStatus(String userId, InterviewStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Interview i where i.id = :id and i.userId = :userId")
    Optional<Interview> findByIdAndUserIdForUpdate(@Param("id") Long id, @Param("userId") String userId);

    Optional<Interview> findByIdAndUserId(Long id, String userId);

    List<Interview> findByUserIdAndStatusAndEndedAtIsNullOrderByCreatedAt(String userId , InterviewStatus status);

    @Modifying
    @Query("update Interview i set i.answeredQuestions = i.answeredQuestions + 1 where i.id = :id")
    void incrementAnswered(@Param("id") Long id);

    @Modifying
    @Query("update Interview i set i.totalCount = i.totalCount + 1 where i.id = :id")
    void incrementTotalCount(@Param("id") Long id);

}
