package com.mockio.feedback_service.kafka.consumer;

import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedback;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.feedback_service.domain.InterviewSummaryFeedback;
import com.mockio.feedback_service.repository.SummaryFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.ANSWER_NOT_FOUND;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class SummaryFeedbackTxService {

    private final SummaryFeedbackRepository summaryFeedbackRepository;

    @Transactional
    public InterviewSummaryFeedback ensurePending(Long interviewId) {
        try {
            return summaryFeedbackRepository.findByInterviewId(interviewId)
                    .orElseGet(() -> summaryFeedbackRepository.save(InterviewSummaryFeedback.create(interviewId)));
        } catch (DataIntegrityViolationException e) {
            return summaryFeedbackRepository.findByInterviewId(interviewId)
                    .orElseThrow(() -> new CustomApiException(ANSWER_NOT_FOUND.getHttpStatus(), ANSWER_NOT_FOUND, ANSWER_NOT_FOUND.getMessage()));
        }
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void markSucceeded(Long interviewId, GeneratedSummaryFeedback result) {
        InterviewSummaryFeedback isf = summaryFeedbackRepository.findByInterviewId(interviewId)
                .orElseThrow(() -> new CustomApiException(ANSWER_NOT_FOUND.getHttpStatus(), ANSWER_NOT_FOUND, ANSWER_NOT_FOUND.getMessage()));

        // 이미 성공이면 스킵 (중복 처리 방지)
        if (isf.successChk()) return;

        isf.succeed(
                result.summaryText(),
                result.totalScore(),
                result.strengths(),
                result.improvements(),
                result.provider(),
                result.model(),
                result.promptVersion(),
                result.temperature()
        );
    }

}
