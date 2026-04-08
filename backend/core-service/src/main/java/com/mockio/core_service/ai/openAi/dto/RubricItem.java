package com.mockio.core_service.ai.openAi.dto;

import lombok.Data;

@Data
public class RubricItem {
    private String key;
    private String label;
    private int maxScore;
}
