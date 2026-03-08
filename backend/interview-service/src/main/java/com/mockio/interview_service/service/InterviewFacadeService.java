package com.mockio.interview_service.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.interview_service.config.FeedbackServiceClient;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.response.FeedbackDetailResponse;
import com.mockio.interview_service.repository.InterviewAnswerRepository;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_FORBIDDEN;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewFacadeService {

    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final FeedbackServiceClient feedbackServiceClient;

    public FeedbackDetailResponse readFeedback(String userId, Long questionId) {
        InterviewQuestion question = interviewQuestionRepository
                .findByIdWithInterview(questionId)
                .orElseThrow(() -> new CustomApiException(
                        INTERVIEW_NOT_FOUND.getHttpStatus(),
                        INTERVIEW_NOT_FOUND,
                        INTERVIEW_NOT_FOUND.getMessage()
                ));
        InterviewUserCheck(userId, question);

        InterviewAnswer answer = interviewAnswerRepository
                .findByQuestionId(questionId)
                .orElse(null);

        if (answer == null) {
            return null;
        }

        return feedbackServiceClient.getFeedbackDetail(answer.getId());
    }

    private static void InterviewUserCheck(String userId, InterviewQuestion question) {
        if (!question.getInterview().getUserId().equals(userId)) {
            throw new CustomApiException(
                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                    INTERVIEW_FORBIDDEN,
                    INTERVIEW_FORBIDDEN.getMessage()
            );
        }
    }
}