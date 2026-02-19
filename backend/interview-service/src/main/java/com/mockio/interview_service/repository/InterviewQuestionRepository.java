package com.mockio.interview_service.repository;

import com.mockio.interview_service.constant.QuestionType;
import com.mockio.interview_service.domain.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion , Long> {

    boolean existsByInterviewId(Long interviewId);

    List<InterviewQuestion> findAllByInterviewIdOrderBySeqAsc(Long interviewId);

    Optional<InterviewQuestion> findByIdAndInterviewId(Long id , Long interviewId);

    Optional<InterviewQuestion> findFirstByInterviewIdAndSeqGreaterThanOrderBySeqAsc(Long interviewId, Integer seq);

    int countByInterviewId(Long interviewId);

    int countByInterviewIdAndType(Long interviewId , QuestionType type);

    boolean existsByInterviewIdAndParentQuestionIdAndType(Long interviewId, Long parentQuestionId, QuestionType type);

}
