package com.mockio.ai_service.ollama.dto.response;

/**
 * Ollama Chat Completions API 응답을 표현하는 DTO.
 *
 * <p>응답 결과는 하나 이상의 Choice로 구성되며,
 * 각 Choice는 생성된 메시지(Message)를 포함한다.</p>
 */

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


@Schema(description = "OpenAI Chat Completion API 응답")
public record OllamaChatResponse(
        @Schema(description = "생성된 응답 선택지 목록")
        List<Choice> choices
) {

    /**
     * OpenAI가 생성한 개별 응답 선택지.
     */
    @Schema(description = "생성된 응답 선택지")
    public record Choice(
            @Schema(description = "생성된 메시지")
            Message message
    ) {

        /**
         * OpenAI Chat API 메시지 객체.
         */
        @Schema(description = "생성된 메시지")
        public record Message(
                @Schema(description = "메시지 역할", example = "assistant")
                String role,
                @Schema(description = "생성된 응답 내용", example = "Spring의 @Transactional은 프록시 기반으로 동작합니다.")
                String content
        ) {}
    }

}
