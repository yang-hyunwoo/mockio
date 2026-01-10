package com.mockio.ai_service.openAi.dto.request;

import java.util.List;

public record OpenAIChatRequest(
        String model,
        List<Message> messages,
        double temperature
) {
    public record Message(String role, String content) {}
}
