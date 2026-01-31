package com.mockio.interview_service.service;

import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.kafka.dto.response.InterviewAnswerDetailResponse;
import com.mockio.interview_service.repository.InterviewAnswerRepository;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class InternalInterviewReadService {

    private final InterviewAnswerRepository interviewAnswerRepository;

    @Transactional
    public InterviewAnswerDetailResponse getInterviewDetail(Long answerId,String userId) {
        InterviewAnswer interviewAnswer = interviewAnswerRepository.findById(answerId)
                .orElseThrow(() -> new CustomApiException(INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()));
        if (interviewAnswer.getQuestion().getInterview().getUserId().equals(userId)) {

            InterviewAnswerDetailResponse interviewAnswerDetailResponse = new InterviewAnswerDetailResponse(
                    interviewAnswer.getId(),
                    interviewAnswer.getQuestion().getInterview().getId(),
                    interviewAnswer.getId(),
                    interviewAnswer.getAttempt(),
                    interviewAnswer.getQuestion().getQuestionText(),
                    interviewAnswer.getAnswerText(),
                    interviewAnswer.getAnswerDurationSeconds()
            );
            return interviewAnswerDetailResponse;

        } else {
            throw new CustomApiException(INTERVIEW_FORBIDDEN.getHttpStatus(),
                    INTERVIEW_FORBIDDEN,
                    INTERVIEW_FORBIDDEN.getMessage());
        }
    }
}
