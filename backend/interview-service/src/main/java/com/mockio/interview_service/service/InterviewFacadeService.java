package com.mockio.interview_service.service;

import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.interview_service.Mapper.InterviewMapper;
import com.mockio.interview_service.config.FeedbackServiceClient;
import com.mockio.interview_service.domain.Interview;
import com.mockio.interview_service.domain.InterviewAnswer;
import com.mockio.interview_service.domain.InterviewQuestion;
import com.mockio.interview_service.dto.response.FeedbackDetailResponse;
import com.mockio.interview_service.dto.response.FeedbackTotalDetailResponse;
import com.mockio.interview_service.dto.response.InterviewResultResponse;
import com.mockio.interview_service.repository.InterviewAnswerRepository;
import com.mockio.interview_service.repository.InterviewQuestionRepository;
import com.mockio.interview_service.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.*;
import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.INTERVIEW_NOT_END;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewFacadeService {

    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final FeedbackServiceClient feedbackServiceClient;
    private final InterviewRepository interviewRepository;

    public FeedbackDetailResponse readFeedback(Long userId, Long questionId) {
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

    public InterviewResultResponse getInterviewHistoryDetail(Long interviewId , Long userId) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, userId)
                .orElseThrow(
                        () -> new CustomApiException(INTERVIEW_FORBIDDEN.getHttpStatus(),
                                INTERVIEW_FORBIDDEN,
                                INTERVIEW_FORBIDDEN.getMessage()
                        )
                );
        if(interview.getStatus().equals(InterviewStatus.ENDED)) {
            List<InterviewQuestion> questions = interviewQuestionRepository.findAllByInterviewIdOrderBySeqAsc(interview.getId());
            List<Long> questionIds = questions.stream()
                    .map(InterviewQuestion::getId)
                    .toList();
            List<InterviewAnswer> answers =
                    interviewAnswerRepository.findAllByQuestionIdInOrderByIdAsc(questionIds);
            FeedbackTotalDetailResponse interviewHistoryDetail = feedbackServiceClient.getInterviewHistoryDetail(interviewId);

            return InterviewMapper.fromResult(interview,
                    questions,
                    answers,
                    interviewHistoryDetail);
        } else {
            throw new CustomApiException(INTERVIEW_NOT_END.getHttpStatus(),
                    INTERVIEW_NOT_END,
                    INTERVIEW_NOT_END.getMessage());
        }

    }



    private static void InterviewUserCheck(Long userId, InterviewQuestion question) {
        if (!Objects.equals(question.getInterview().getUserId(), userId)) {
            throw new CustomApiException(
                    INTERVIEW_FORBIDDEN.getHttpStatus(),
                    INTERVIEW_FORBIDDEN,
                    INTERVIEW_FORBIDDEN.getMessage()
            );
        }
    }
}