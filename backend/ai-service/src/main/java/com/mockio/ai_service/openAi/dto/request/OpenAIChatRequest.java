package com.mockio.ai_service.openAi.dto.request;

/**
 * OpenAI Chat Completions API 요청 바디를 표현하는 DTO.
 *
 * <p>모델 정보, 대화 메시지 목록, 생성 파라미터(temperature)를 포함하며
 * OpenAI Chat Completion API 규격에 맞춰 직렬화된다.</p>
 */

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record OpenAIChatRequest(
        @Schema(description = "사용할 OpenAI 모델", example = "gpt-4o-mini")
        String model,
        @Schema(description = "대화 메시지 목록")
        List<Message> messages,
        @Schema(description = "응답 생성 다양성(0~2)", example = "0.7")
        double temperature
) {

    /**
     * OpenAI Chat API 메시지 객체.
     */
    public record Message(
            @Schema(description = "메시지 역할", example = "user")
            String role,
            @Schema(description = "메시지 내용", example = "Spring Boot의 @Transactional 동작 원리를 설명하세요.")
            String content
    ) {}
}
