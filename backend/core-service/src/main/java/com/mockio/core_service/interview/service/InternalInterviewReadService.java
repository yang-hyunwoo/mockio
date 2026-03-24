package com.mockio.core_service.interview.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.domain.InterviewAnswer;
import com.mockio.core_service.interview.kafka.dto.response.InternalInterviewAnswerDetailResponse;
import com.mockio.core_service.interview.repository.InterviewAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class InternalInterviewReadService {

    private final InterviewAnswerRepository interviewAnswerRepository;

    @Transactional(readOnly = true)
    public InternalInterviewAnswerDetailResponse getInterviewDetail(Long answerId) {
        InterviewAnswer interviewAnswer = interviewAnswerRepository.findById(answerId)
                .orElseThrow(() -> new CustomApiException(INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()));
            InternalInterviewAnswerDetailResponse interviewAnswerDetailResponse = new InternalInterviewAnswerDetailResponse(
                    interviewAnswer.getId(),
                    interviewAnswer.getQuestion().getInterview().getId(),
                    interviewAnswer.getId(),
                    interviewAnswer.getAttempt(),
                    interviewAnswer.getQuestion().getQuestionText(),
                    interviewAnswer.getAnswerText(),
                    interviewAnswer.getAnswerDurationSeconds()
            );
            return interviewAnswerDetailResponse;
    }



}
