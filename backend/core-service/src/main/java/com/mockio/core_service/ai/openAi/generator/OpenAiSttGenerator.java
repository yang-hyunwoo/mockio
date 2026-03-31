package com.mockio.core_service.ai.openAi.generator;


import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.question.GenerateSttCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedStt;
import com.mockio.common_ai_contractor.generator.question.SttGenerator;
import com.mockio.core_service.ai.openAi.client.OpenAiAudioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="ai.generator" , havingValue = "openai")
@RequiredArgsConstructor
public class OpenAiSttGenerator implements SttGenerator {

    private final OpenAiAudioClient client;

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    @Override
    public GeneratedStt generate(GenerateSttCommand command) {
        String text = client.transcribe(
                command.inputStream(),
                command.filename(),
                command.contentType(),
                "gpt-4o-mini-transcribe"
        );
        return new GeneratedStt(text);
    }

}
