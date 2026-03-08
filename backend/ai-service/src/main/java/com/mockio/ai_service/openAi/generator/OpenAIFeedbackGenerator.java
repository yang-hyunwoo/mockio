package com.mockio.ai_service.openAi.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.ai_service.openAi.client.SpringAiOpenAIClient;
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
public class OpenAIFeedbackGenerator implements FeedbackGenerator {

    private final SpringAiOpenAIClient client;
    private final String MODEL = "gpt-4o-mini";
    private final ObjectMapper objectMapper;

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    @Override
    @CircuitBreaker(name = "openaiFeedbackChat")
    public GeneratedFeedback generate(GenerateFeedbackCommand command) {
        Double temperature = 0.3;

        String system = """
                당신은 %s 기술면접관입니다. 난이도(%s)에 맞춰 지원자의 답변을 평가하세요.
                모든 출력은 반드시 한국어로 작성합니다.
                영어 문장 사용 금지, 기술 용어만 영어 허용.
                
                반드시 JSON 객체만 출력하세요.
                JSON 외의 텍스트, 설명, 마크다운, 코드블록, 번호는 절대 포함하지 마세요.
                
                반드시 아래 스키마를 따르세요.
                {
                  "score": 0,
                  "summary": "",
                  "strengths": [],
                  "improvements": [],
                  "modelAnswer": ""
                }
                
                필드 규칙:
                - score: 0~100 정수
                - summary: 전체 평가를 요약한 총평 1~2문장
                - strengths: 강점 0~2개, 각 항목은 1문장
                - improvements: 개선점 1~3개, 각 항목은 1문장
                - modelAnswer: 모범답변 3~6문장
                - 모든 필드는 반드시 포함해야 합니다.
                - strengths, improvements는 반드시 문자열 배열로 출력해야 합니다.
                
                평가 원칙:
                - 평가는 지나치게 긍정적으로 미화하지 마세요.
                - 지원자의 답변이 부적절하거나 공격적이거나 협업 역량을 크게 해치는 경우, 그 문제를 명확히 지적하세요.
                - 존재하지 않는 강점을 억지로 만들지 마세요.
                - 강점이 거의 없으면 strengths는 빈 배열 또는 1개만 작성해도 됩니다.
                - score, summary, strengths, improvements의 톤과 수위는 반드시 일관되게 유지하세요.
                - 낮은 점수인 경우 그에 맞는 비판적이고 구체적인 피드백을 제공하세요.
                
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
