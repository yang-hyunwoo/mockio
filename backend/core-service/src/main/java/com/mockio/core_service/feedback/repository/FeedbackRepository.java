package com.mockio.core_service.feedback.repository;

import com.mockio.core_service.feedback.domain.InterviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<InterviewFeedback , Long> {

    Optional<InterviewFeedback> findByAnswerId(Long answerId);

    List<InterviewFeedback> findByInterviewIdOrderByIdAsc(Long interviewId);
}
