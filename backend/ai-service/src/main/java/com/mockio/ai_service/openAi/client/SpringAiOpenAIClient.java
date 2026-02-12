package com.mockio.ai_service.openAi.client;

import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.ai_service.util.AIChatClient;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpringAiOpenAIClient implements AIChatClient {

    private final ChatModel chatModel;

    @Override
    public String chat(String model, String prompt, String commandText, Double temperature) {
        try {
            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .model(model)                 // yml에 기본 모델 두고 여기선 override 가능
                    .temperature(temperature)
                    .build();
            Prompt springPrompt = new Prompt(
                    List.of(
                            new SystemMessage(commandText),
                            new UserMessage(prompt)
                    ),
                    options
            );

            var res = chatModel.call(springPrompt);
            String content = res.getResult().getOutput().getText();

            if (content == null || content.isBlank()) {
                throw new CustomApiException(
                        500,
                        AIErrorEnum.ILLEGALSTATE,
                        AIErrorEnum.ILLEGALSTATE.getMessage()
                );
            }
            return content;

        } catch (Exception e) {
            throw mapToCustomApiException(e);
        }
    }

    private CustomApiException mapToCustomApiException(Throwable t) {
        // ✅ 여기서 Spring AI/OpenAI 예외를 AIErrorEnum으로 매핑
        // Spring AI 예외 타입은 버전에 따라 조금 다른데,
        // 핵심은 429/401/403/400/timeout 등을 잡아내는 것
        String msg = t.toString();

        // 429 rate limit
        if (msg.contains("429") || msg.toLowerCase().contains("rate limit")) {
            return new CustomApiException(429, AIErrorEnum.RATE_LIMIT, AIErrorEnum.RATE_LIMIT.getMessage());
        }
        // 401/403
        if (msg.contains("401") || msg.contains("403")) {
            return new CustomApiException(401, AIErrorEnum.UNAUTHORIZED, AIErrorEnum.UNAUTHORIZED.getMessage());
        }
        // 400
        if (msg.contains("400")) {
            return new CustomApiException(400, AIErrorEnum.BAD_REQUEST, AIErrorEnum.BAD_REQUEST.getMessage());
        }

        // timeout
        if (isTimeout(t)) {
            return new CustomApiException(
                    500,
                    AIErrorEnum.ILLEGALSTATE,
                    "외부 AI 서버 응답이 지연되었습니다."
            );
        }

        log.warn("Spring AI(OpenAI) unexpected error: {}", t.toString());
        return new CustomApiException(500, AIErrorEnum.ILLEGALSTATE, AIErrorEnum.ILLEGALSTATE.getMessage());
    }

    private boolean isTimeout(Throwable t) {
        Throwable c = t.getCause();
        return t.getClass().getName().toLowerCase().contains("timeout")
                || (c != null && c.getClass().getName().toLowerCase().contains("timeout"));
    }
}