package com.mockio.interview_service.repository;

import com.mockio.interview_service.domain.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

    @Query("select max(a.attempt) from InterviewAnswer a where a.question.id = :questionId")
    Optional<Integer> findMaxAttemptByQuestionId(@Param("questionId") Long questionId);
}
