package com.mockio.core_service.interview.repository;

import com.mockio.core_service.interview.domain.InterviewCompareQuestion;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InterviewCompareQuestionRepository extends JpaRepository<InterviewCompareQuestion, Long> {

    @Modifying
    @Query("""
                update InterviewCompareQuestion q
                   set q.status = 'PROCESSING',
                       q.updatedAt = CURRENT_TIMESTAMP
                 where q.id = :id
                   and q.status = 'PENDING'
            """)
    int markProcessingIfPending(@Param("id") Long id);

    Optional<InterviewCompareQuestion> findByInterviewIdAndCurrentQuestionIdAndPrevQuestionId(
            Long interviewId,
            Long currentQuestionId,
            Long prevQuestionId
    );

}
