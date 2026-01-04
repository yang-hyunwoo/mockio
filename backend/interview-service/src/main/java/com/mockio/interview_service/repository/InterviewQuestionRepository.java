package com.mockio.interview_service.repository;

import com.mockio.interview_service.domain.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion , Long> {

    boolean existsByInterviewId(Long interviewId);

    List<InterviewQuestion> findAllByInterviewIdOrderBySeqAsc(Long interviewId);
}
