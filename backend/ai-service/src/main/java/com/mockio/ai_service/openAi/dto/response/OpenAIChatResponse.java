package com.mockio.ai_service.openAi.dto.response;

import java.util.List;

public record OpenAIChatResponse(
        List<Choice> choices
) {
    public record Choice(Message message) {
        public record Message(String role, String content) {}
    }
}
