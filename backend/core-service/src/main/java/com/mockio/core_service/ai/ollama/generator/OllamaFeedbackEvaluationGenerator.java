package com.mockio.core_service.ai.ollama.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackEvaluationGenerator;
import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedbackEvaluation;
import com.mockio.core_service.ai.ollama.client.OllamaClient;
import com.mockio.core_service.ai.util.AiResponseSanitizer;
import com.mockio.core_service.ai.util.PromptLoader;
import com.mockio.core_service.ai.util.RubricProvider;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaFeedbackEvaluationGenerator implements FeedbackEvaluationGenerator {

    private final OllamaClient client;
    private final PromptLoader promptLoader;
    private final AiResponseSanitizer sanitizer;
    private static final String MODEL = "llama3.1:8b";
    private final ObjectMapper objectMapper;
    private final RubricProvider rubricProvider;
    private String commandPrompt;
    private String systemPrompt;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "ai/prompt/feedback/";
        commandPrompt = promptLoader.load(absPath + "feedback-evaluation-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "feedback-prompt-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OLLAMA;
    }

    @Override
    @CircuitBreaker(name = "ollamaFeedbackChat")
    public GeneratedFeedbackEvaluation generate(GenerateFeedbackCommand command) {
        Double temperature = 0.3;

        String commandText = null;
        try {
             commandText = commandPrompt.formatted(
                    command.track(),
                    command.difficulty(),
                    command.primaryTag(),
                    objectMapper.writeValueAsString(
                            rubricProvider.getByTrack(command.track())
                    )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String prompt = systemPrompt.formatted(
                command.track(),
                command.difficulty(),
                command.questionText(),
                command.answerText()
        );

        String answer = client.chat(MODEL, prompt, commandText, temperature);
        try {
           return objectMapper.readValue(answer, GeneratedFeedbackEvaluation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
