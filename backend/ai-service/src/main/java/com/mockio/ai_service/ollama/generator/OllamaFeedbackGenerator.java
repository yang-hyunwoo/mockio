package com.mockio.ai_service.ollama.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.ai_service.ollama.client.OllamaClient;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.FeedbackGenerator;
import com.mockio.common_ai_contractor.generator.feedback.GenerateFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedFeedback;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaFeedbackGenerator implements FeedbackGenerator {

    private final OllamaClient client;
    private final String MODEL = "llama3.1:8b";
    private final ObjectMapper objectMapper;


    @Override
    public AiEngine engine() {
        return AiEngine.OLLAMA;
    }

    @Override
    @CircuitBreaker(name = "ollamaFeedbackChat")
    public GeneratedFeedback generate(GenerateFeedbackCommand command) {
        Double temperature = 0.3;

        String system = """
                당신은 %s 기술면접관입니다. 난이도(%s)에 맞춰 평가하세요.
                
                반드시 JSON 객체만 출력하세요.
                JSON 외의 텍스트/설명/마크다운/코드블록/번호는 절대 포함하지 마세요.
                
                반드시 포함해야 하는 필드:
                - score: 0~100 정수
                - strengths: 강점 2~3문장
                - improvements: 개선점 2~3문장
                - modelAnswer: 모범답변 3~6문장
                
                출력 예시(형식 참고용):
                {
                  "score": 85,
                  "strengths": "강점 예시입니다.",
                  "improvements": "개선점 예시입니다.",
                  "modelAnswer": "모범답변 예시입니다.",
                }
                
                평가 기준:
                - 기술적 정확성
                - 실무 적합성
                - 개념 이해도
                - 설명의 명확성
                """.formatted(
                command.track(),
                command.difficulty()
        );

        String user = """
                    조건:
                    - 트랙: %s
                    - 난이도: %s
                
                    면접 질문:
                    %s
                
                    지원자 답변:
                    %s
                """.formatted(
                command.track(),
                command.difficulty(),
                command.questionText(),
                command.answerText()
        );

        String answer = client.chat(MODEL, user, system, temperature);

        Integer score = extractScoreSafely(answer);
         return new GeneratedFeedback(
                 answer,
                score,
                "OPENAI",
                MODEL,
                "v1",
                temperature
        );

    }

    private Integer extractScoreSafely(String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            JsonNode node = root.get("score");
            if (node == null || node.isNull()) return null;

            int score;
            if (node.isInt()) score = node.asInt();
            else if (node.isNumber()) score = (int) Math.round(node.asDouble());
            else if (node.isTextual()) {
                String digits = node.asText().replaceAll("[^0-9]", "");
                if (digits.isBlank()) return null;
                score = Integer.parseInt(digits);
            } else return null;

            return (score >= 0 && score <= 100) ? score : null;
        } catch (Exception e) {
            log.warn("score extraction failed. raw={}", truncate(rawJson), e);
            return null;
        }
    }

    private String truncate(String s) {
        if (s == null) return "";
        return s.length() > 500 ? s.substring(0, 500) + "...(truncated)" : s;
    }

}
