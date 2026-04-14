package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareQuestion;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareQuestionCommand;
import com.mockio.core_service.interview.forward.ai.AIServiceClient;
import com.mockio.core_service.interview.kafka.dto.request.InterviewCompareQuestionPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CompareQuestionProcessor {

    private final InterviewCompareQuestionService interviewCompareQuestionService;
    private final AIServiceClient aiServiceClient;

    /**
     * 비교 질문 평가 생성
     * @param compareQuestionId
     * @param payload
     */
    public void process(Long compareQuestionId, InterviewCompareQuestionPayload payload) {
        int updated = interviewCompareQuestionService.markProcessingIfPending(compareQuestionId);

        if (updated == 0) {
            return;
        }

        try {
            GeneratedCompareQuestionCommand generatedCompareQuestionCommand = new GeneratedCompareQuestionCommand(
                    payload.track(),
                    payload.feedbackStyle(),
                    payload.evaluationCriteria(),
                    payload.questionTitle(),
                    payload.currentAnswer(),
                    payload.prevAnswer()
            );

            GeneratedCompareQuestion response = aiServiceClient.generateCompareQuestion(generatedCompareQuestionCommand);
            interviewCompareQuestionService.complete(compareQuestionId, response);
        } catch (Exception e) {
            interviewCompareQuestionService.fail(compareQuestionId, e.getMessage());
            throw e;
        }
    }

}
