package com.mockio.interview_service.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.kafka.dto.response.InterviewAnswerDetailResponse;
import com.mockio.interview_service.repository.InterviewAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class InternalInterviewReadService {

    private final InterviewAnswerRepository interviewAnswerRepository;

    @Transactional(readOnly = true)
    public InterviewAnswerDetailResponse getInterviewDetail(Long answerId) {
        InterviewAnswer interviewAnswer = interviewAnswerRepository.findById(answerId)
                .orElseThrow(() -> new CustomApiException(INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()));
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
    }


    @Transactional(readOnly = true)
    public List<InterviewAnswerDetailResponse> getInterviewList(Long interviewId) {
        return interviewAnswerRepository.findDetailsByInterviewId(interviewId);
    }


}
