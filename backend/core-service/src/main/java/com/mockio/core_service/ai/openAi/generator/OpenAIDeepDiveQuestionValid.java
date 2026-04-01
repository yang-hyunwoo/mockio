package com.mockio.core_service.ai.openAi.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveQuestionValid;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveValid;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.core_service.ai.openAi.client.SpringAiOpenAIClient;
import com.mockio.core_service.ai.util.PromptLoader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAIDeepDiveQuestionValid implements DeepDiveQuestionValid {

    private final PromptLoader promptLoader;
    private final SpringAiOpenAIClient client;
    private final String MODEL = "gpt-4o-mini";
    private String commandPrompt;
    private String systemPrompt;
    private final ObjectMapper objectMapper;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "ai/prompt/deepdive/";
        commandPrompt = promptLoader.load(absPath + "deep-dive-command-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "deep-dive-prompt-check-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    @Override
    public DeepDiveValid generateValid(GenerateDeepDiveCommand command) {
        String basicQText = (command == null || command.basicQuestion() == null) ? "N/A" : command.basicQuestion();
        String basicAText = (command == null || command.basicAnswer() == null) ? "N/A" : command.basicAnswer();
        String qText = (command == null || command.question() == null) ? "N/A" : command.question();
        String aText = (command == null || command.answer() == null) ? "" : command.answer();

        String commandText = commandPrompt.formatted(command.interviewTrack());

        String prompt = systemPrompt.formatted(command.interviewTrack(),
                command.interviewTrack(),
                command.interviewDifficulty(),
                basicQText,
                basicAText,
                qText,
                aText
        );
        Double temperature = 0.2;
        String answer = client.chat(MODEL, prompt,commandText,temperature);
        DeepDiveValid q;
        try {
            q = objectMapper.readValue(answer, DeepDiveValid.class);
        } catch (Exception e) {
            q = new DeepDiveValid(
                    false,
                    "검증 실패",
                    "검증 실패"
            );
        }
        return q;
    }
}
