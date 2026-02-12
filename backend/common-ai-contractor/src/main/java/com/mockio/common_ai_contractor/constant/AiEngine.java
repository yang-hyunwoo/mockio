package com.mockio.common_ai_contractor.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AiEngine {
    OPENAI("open-ai"),
    OLLAMA("ollama"),
    FAKE("fake"),
    ;


    private final String label;

}

