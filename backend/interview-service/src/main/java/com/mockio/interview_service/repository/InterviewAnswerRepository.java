package com.mockio.interview_service.repository;

import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.kafka.dto.response.InterviewAnswerDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

    @Query("select max(a.attempt) from InterviewAnswer a where a.question.id = :questionId")
    Optional<Integer> findMaxAttemptByQuestionId(@Param("questionId") Long questionId);

    Optional<InterviewAnswer> findByQuestionIdAndIdempotencyKey(Long questionId, String idempotencyKey);

    @Query("""
        select new com.mockio.interview_service.kafka.dto.response.InterviewAnswerDetailResponse(
            a.id,
            q.interview.id,
            q.id,
            a.attempt,
            q.questionText,
            a.answerText,
            a.answerDurationSeconds
        )
        from InterviewAnswer a
        join a.question q
        where q.interview.id = :interviewId
        order by q.id asc, a.attempt desc
    """)
    List<InterviewAnswerDetailResponse> findDetailsByInterviewId(Long interviewId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update InterviewAnswer a
                   set a.current = false
                 where a.question.id = :questionId
                   and a.current = true
            """)
    int unsetCurrentByQuestionId(@Param("questionId") Long questionId);

}
