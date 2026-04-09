package com.mockio.core_service.interview.service;

import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.interview.dto.request.InterviewGenerateContext;
import com.mockio.core_service.interview.dto.request.RetryInterviewRequest;
import com.mockio.core_service.interview.dto.request.StartInterviewRequest;
import com.mockio.core_service.interview.dto.response.InterviewQuestionReadResponse;
import com.mockio.core_service.interview.forward.ai.AIServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.mockio.common_ai_contractor.constant.InterviewErrorCode.AI_SERVICE_FAILED;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final AIServiceClient aiServiceClient;
    private final InterviewCreateService interviewCreateService;
    private final InterviewQuestionTxService interviewQuestionTxService;
    private static final int ADD_QUESTION_COUNT =5;

    public InterviewQuestionReadResponse startInterview(Long userId, StartInterviewRequest request) {
        Long interviewId = interviewCreateService.generateInterview(userId, request);
        return generateAndSaveQuestions(interviewId, userId);
    }

    public InterviewQuestionReadResponse generateAndSaveQuestions(Long interviewId, Long userId) {
        InterviewGenerateContext context = interviewQuestionTxService.prepareGenerate(interviewId, userId);

        if (context.alreadyGenerated()) {
            return interviewQuestionTxService.getQuestions(interviewId, userId);
        }

        GeneratedQuestion generatedQuestion;
        try {
            GenerateQuestionCommand cmd = new GenerateQuestionCommand(
                    userId,
                    context.track(),
                    context.difficulty(),
                    context.interviewMode(),
                    context.answerTimeSeconds(),
                    context.count() +ADD_QUESTION_COUNT
            );

            generatedQuestion = aiServiceClient.generateQuestions(cmd);

        } catch (Exception e) {
            interviewQuestionTxService.failGenerate(interviewId, userId, e.getMessage());
            throw new CustomApiException(
                    AI_SERVICE_FAILED.getHttpStatus(),
                    AI_SERVICE_FAILED,
                    AI_SERVICE_FAILED.getMessage()
            );
        }

        interviewQuestionTxService.completeGenerate(interviewId, userId, generatedQuestion);
        return interviewQuestionTxService.getQuestions(interviewId, userId);
    }

    public InterviewQuestionReadResponse retryInterview(Long userId, RetryInterviewRequest request) {
        return interviewQuestionTxService.retryInterview(userId, request);
    }
}