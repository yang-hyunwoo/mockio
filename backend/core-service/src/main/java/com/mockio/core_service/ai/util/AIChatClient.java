package com.mockio.core_service.ai.util;

public interface AIChatClient {
    String chat(String model, String prompt, String commandText,Double temperature);
}
