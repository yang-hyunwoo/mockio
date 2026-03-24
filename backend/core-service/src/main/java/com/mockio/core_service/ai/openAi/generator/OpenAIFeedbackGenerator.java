package com.mockio.core_service.ai.openAi.generator;

import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackGenerator;
import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import com.mockio.core_service.ai.openAi.client.SpringAiOpenAIClient;
import com.mockio.core_service.ai.util.AiResponseSanitizer;
import com.mockio.core_service.ai.util.PromptLoader;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIFeedbackGenerator implements FeedbackGenerator {

    private final SpringAiOpenAIClient client;
    private final PromptLoader promptLoader;
    private final AiResponseSanitizer sanitizer;
    private final String MODEL = "gpt-4o-mini";
    private String commandPrompt;
    private String systemPrompt;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "ai/prompt/feedback/";
        commandPrompt = promptLoader.load(absPath + "feedback-command-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "feedback-prompt-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    @Override
    @CircuitBreaker(name = "openaiFeedbackChat")
    public GeneratedFeedback generate(GenerateFeedbackCommand command) {
        Double temperature = 0.3;

        String commandText = commandPrompt.formatted(
                command.track(),
                command.difficulty(),
                command.feedbackStyle()
        );

        String prompt = systemPrompt.formatted(
                command.track(),
                command.difficulty(),
                command.questionText(),
                command.answerText()
        );

        String answer = client.chat(MODEL, prompt, commandText, temperature);

        Integer score = sanitizer.extractScoreSafely(answer,"score");
         return new GeneratedFeedback(
                 answer,
                score,
                "OPENAI",
                MODEL,
                "v1",
                temperature
        );
    }

}
