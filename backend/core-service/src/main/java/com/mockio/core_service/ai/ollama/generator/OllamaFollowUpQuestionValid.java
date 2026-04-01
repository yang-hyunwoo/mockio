package com.mockio.core_service.ai.ollama.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionValid;
import com.mockio.common_ai_contractor.generator.followup.FollowupValid;
import com.mockio.core_service.ai.ollama.client.OllamaClient;
import com.mockio.core_service.ai.util.PromptLoader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OllamaFollowUpQuestionValid implements FollowUpQuestionValid {

    private final PromptLoader promptLoader;
    private final OllamaClient client;
    private static final String MODEL = "llama3.1:8b";
    private String commandPrompt;
    private String systemPrompt;
    private final ObjectMapper objectMapper;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "ai/prompt/followup/";
        commandPrompt = promptLoader.load(absPath + "followup-command-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "followup-prompt-check-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OLLAMA;
    }


    @Override
    public FollowupValid generateValid(FollowUpQuestionCommand command) {
        var qa = command.recentQa();
        String qText = (qa == null || qa.question() == null) ? "N/A" : qa.question();
        String aText = (qa == null || qa.answer() == null) ? "" : qa.answer();

        String commandText = commandPrompt.formatted(command.interviewTrack());

        String prompt = systemPrompt.formatted(
                command.interviewTrack(),
                command.interviewDifficulty(),
                qText,
                aText
        );
        Double temperature = 0.2;
        String answer = client.chat(MODEL, prompt,commandText,temperature);
        FollowupValid q;
        try {
            q = objectMapper.readValue(answer, FollowupValid.class);
        } catch (Exception e) {
            q = new FollowupValid(
                    false,
                    "검증 실패",
                    "검증 실패"
            );
        }
        return q;
    }

}
