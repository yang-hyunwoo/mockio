package com.mockio.interview_service.repository;

import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.interview_service.domain.Interview;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Optional<Interview> findActiveByUserIdAndStatus(String userId, InterviewStatus status);

    Optional<Interview> findActiveByIdAndStatus(Long id, InterviewStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Interview i where i.id = :id and i.userId = :userId")
    Optional<Interview> findByIdAndUserIdForUpdate(@Param("id") Long id, @Param("userId") String userId);

    Optional<Interview> findByIdAndUserId(Long id, String userId);
}
