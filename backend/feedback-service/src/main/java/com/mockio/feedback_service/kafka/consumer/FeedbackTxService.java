package com.mockio.feedback_service.kafka.consumer;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.feedback_service.domain.InterviewFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import com.mockio.feedback_service.repository.InterviewFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.ANSWER_NOT_FOUND;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class FeedbackTxService {

    private final InterviewFeedbackRepository interviewFeedbackRepository;

    @Transactional
    public InterviewFeedback ensurePending(Long answerId) {
        try {
            return interviewFeedbackRepository.findByAnswerId(answerId)
                    .orElseGet(() -> interviewFeedbackRepository.save(InterviewFeedback.create(answerId)));
        } catch (DataIntegrityViolationException e) {
            return interviewFeedbackRepository.findByAnswerId(answerId)
                    .orElseThrow(() -> new CustomApiException(ANSWER_NOT_FOUND.getHttpStatus(), ANSWER_NOT_FOUND, ANSWER_NOT_FOUND.getMessage()));
        }

    }

    @Transactional(propagation = REQUIRES_NEW)
    public void markSucceeded(Long answerId, GeneratedFeedback result) {
        InterviewFeedback fb = interviewFeedbackRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new CustomApiException(ANSWER_NOT_FOUND.getHttpStatus(), ANSWER_NOT_FOUND, ANSWER_NOT_FOUND.getMessage()));

        // 이미 성공이면 스킵 (중복 처리 방지)
        if (fb.successChk()) return;

        fb.succeed(
                result.feedbackText(),
                result.score(),
                result.provider(),
                result.model(),
                result.promptVersion(),
                result.temperature()
        );
    }
}
