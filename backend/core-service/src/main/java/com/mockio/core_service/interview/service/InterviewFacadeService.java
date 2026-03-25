package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.constant.InterviewStatus;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.feedback.service.FeedbackService;
import com.mockio.core_service.internalmapper.InternalMapper;
import com.mockio.core_service.interview.Mapper.InterviewMapper;
import com.mockio.core_service.interview.domain.Interview;
import com.mockio.core_service.interview.domain.InterviewAnswer;
import com.mockio.core_service.interview.domain.InterviewQuestion;
import com.mockio.core_service.interview.dto.response.FeedbackDetailResponse;
import com.mockio.core_service.interview.dto.response.FeedbackTotalDetailResponse;
import com.mockio.core_service.interview.dto.response.InterviewResultResponse;
import com.mockio.core_service.interview.repository.InterviewAnswerRepository;
import com.mockio.core_service.interview.repository.InterviewQuestionRepository;
import com.mockio.core_service.interview.repository.InterviewRepository;
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
    private final InterviewRepository interviewRepository;
    private final FeedbackService feedbackService;

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
        return InternalMapper.fromInternalFeedbackDetail(feedbackService.interviewFeedbackRead(answer.getId()));
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

            FeedbackTotalDetailResponse interviewHistoryDetail = InternalMapper.fromInternalFeedbackTotalDetail(feedbackService.getFeedbackDetail(interviewId));

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