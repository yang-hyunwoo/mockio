package com.mockio.core_service.ai.openAi.client;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.util.AIChatClient;
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
                        AIErrorCodeEnum.ILLEGAL_STATE.getHttpStatus(),
                        AIErrorCodeEnum.ILLEGAL_STATE,
                        AIErrorCodeEnum.ILLEGAL_STATE.getMessage()
                );
            }
            return content;

        } catch (Exception e) {
            throw mapToCustomApiException(e);
        }
    }

    private CustomApiException mapToCustomApiException(Throwable t) {
        // Spring AI/OpenAI 예외를 AIErrorEnum으로 매핑
        // Spring AI 예외 타입은 버전에 따라 조금 다른데,
        // 핵심은 429/401/403/400/timeout 등을 잡아내는 것
        String msg = t.toString();

        // 429 rate limit
        if (msg.contains("429") || msg.toLowerCase().contains("rate limit")) {
            return new CustomApiException(429, AIErrorCodeEnum.RATE_LIMIT, AIErrorCodeEnum.RATE_LIMIT.getMessage());
        }
        // 401/403
        if (msg.contains("401") || msg.contains("403")) {
            return new CustomApiException(401, AIErrorCodeEnum.UNAUTHORIZED, AIErrorCodeEnum.UNAUTHORIZED.getMessage());
        }
        // 400
        if (msg.contains("400")) {
            return new CustomApiException(400, AIErrorCodeEnum.BAD_REQUEST, AIErrorCodeEnum.BAD_REQUEST.getMessage());
        }

        // timeout
        if (isTimeout(t)) {
            return new CustomApiException(
                    500,
                    AIErrorCodeEnum.ILLEGAL_STATE,
                    "외부 AI 서버 응답이 지연되었습니다."
            );
        }

        log.warn("Spring AI(OpenAI) unexpected error: {}", t.toString());
        return new CustomApiException(500, AIErrorCodeEnum.ILLEGAL_STATE, AIErrorCodeEnum.ILLEGAL_STATE.getMessage());
    }

    private boolean isTimeout(Throwable t) {
        Throwable c = t.getCause();
        return t.getClass().getName().toLowerCase().contains("timeout")
                || (c != null && c.getClass().getName().toLowerCase().contains("timeout"));
    }

}
