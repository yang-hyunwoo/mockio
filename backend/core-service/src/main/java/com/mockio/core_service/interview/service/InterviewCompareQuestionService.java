package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareQuestion;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.domain.InterviewCompareQuestion;
import com.mockio.core_service.interview.repository.InterviewCompareQuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.COMPARE_QUESTION_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class InterviewCompareQuestionService {

    private final InterviewCompareQuestionRepository compareQuestionRepository;

    public int markProcessingIfPending(Long compareQuestionId) {
        return compareQuestionRepository.markProcessingIfPending(compareQuestionId);
    }

    /**
     * 비교 질문 생성 완료 후 저장
     * @param compareQuestionId
     * @param response
     */
    protected void complete(Long compareQuestionId, GeneratedCompareQuestion response) {
        InterviewCompareQuestion compare = compareQuestionRepository.findById(compareQuestionId)
                .orElseThrow(
                        () -> new CustomApiException(
                                COMPARE_QUESTION_NOT_FOUND.getHttpStatus(),
                                COMPARE_QUESTION_NOT_FOUND,
                                COMPARE_QUESTION_NOT_FOUND.getMessage())
                );
        if (!compare.isProcessing()) {
            return;
        }

        compare.complete(response);
    }

    /**
     * 비교 질문 생성 실패
     * @param compareQuestionId
     * @param errorMessage
     */
    protected void fail(Long compareQuestionId, String errorMessage) {
        InterviewCompareQuestion compare = compareQuestionRepository.findById(compareQuestionId)
                .orElseThrow(
                        () -> new CustomApiException(COMPARE_QUESTION_NOT_FOUND.getHttpStatus(),
                                COMPARE_QUESTION_NOT_FOUND,
                                COMPARE_QUESTION_NOT_FOUND.getMessage())
                );
        if (!compare.isProcessing()) {
            return;
        }
        compare.fail(errorMessage);
    }

}
