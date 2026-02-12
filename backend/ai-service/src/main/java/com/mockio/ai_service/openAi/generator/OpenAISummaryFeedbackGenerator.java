package com.mockio.ai_service.openAi.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.ai_service.openAi.client.SpringAiOpenAIClient;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAISummaryFeedbackGenerator implements SummaryFeedbackGenerator {

    private final SpringAiOpenAIClient client;
    private final ObjectMapper objectMapper;

    private static final String MODEL = "gpt-4o-mini";
    private static final String PROVIDER = "OPENAI";
    private static final String PROMPT_VERSION = "v1";


    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    @Override
    @CircuitBreaker(name = "openaiSummaryChat")
    public GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command) {
        Double temperature = 0.2;

        String system = """
                당신은 %s 기술면접관입니다. 난이도(%s)에 맞춰 전체 인터뷰 답변을 종합 평가하세요.

                반드시 JSON 객체만 출력하세요.
                JSON 외의 텍스트/설명/마크다운/코드블록/번호는 절대 포함하지 마세요.

                반드시 포함해야 하는 필드:
                - totalScore: 0~100 정수
                - summaryText: 총평 3~6문장
                - strengths: 강점 3~5개(문장 또는 bullet 문자열)
                - improvements: 개선점 3~5개(문장 또는 bullet 문자열)

                출력 예시(형식 참고용):
                {
                  "totalScore": 85,
                  "summaryText": "총평 예시입니다.",
                  "strengths": "강점 예시입니다.",
                  "improvements": "개선점 예시입니다."
                }
                """.formatted(command.track(), command.difficulty());

        String user = buildUser(command);

        String rawJson = client.chat(MODEL, user, system, temperature);

        // JSON 파싱 (필드 추출)
        ParsedSummary parsed = parseSummary(rawJson);

        return new GeneratedSummaryFeedback(
                command.interviewId(),
                parsed.summaryText(),
                parsed.totalScore(),
                parsed.strengths(),
                parsed.improvements(),
                PROVIDER,
                MODEL,
                PROMPT_VERSION,
                temperature
        );
    }

    private String buildUser(GeneratedSummaryFeedbackCommand command) {
        var sb = new StringBuilder();
        sb.append("조건:\n")
                .append("- 인터뷰 ID: ").append(command.interviewId()).append("\n")
                .append("- 트랙: ").append(command.track()).append("\n")
                .append("- 난이도: ").append(command.difficulty()).append("\n")
                .append("- 피드백 스타일: ").append(command.feedbackStyle()).append("\n\n");

        sb.append("전체 답변 목록:\n");
        command.items().forEach(it -> {
            sb.append("\n")
                    .append("[answerId=").append(it.answerId()).append(", attempt=").append(it.attempt()).append("]\n")
                    .append("Q: ").append(nullToEmpty(it.questionText())).append("\n")
                    .append("A: ").append(nullToEmpty(it.answerText())).append("\n")
                    .append("durationSec: ").append(it.answerDurationSeconds()).append("\n");
        });
        return sb.toString();
    }

    private ParsedSummary parseSummary(String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);

            Integer totalScore = extractIntSafely(root.get("totalScore"));
            String summaryText = extractTextSafely(root.get("summaryText"));
            String strengths = extractTextSafely(root.get("strengths"));
            String improvements = extractTextSafely(root.get("improvements"));

            // 최소 방어: summaryText 없으면 rawJson 일부라도 남기기
            if (summaryText == null || summaryText.isBlank()) {
                summaryText = "요약 파싱에 실패했습니다. raw=" + truncate(rawJson);
            }

            return new ParsedSummary(
                    summaryText,
                    totalScore,
                    strengths,
                    improvements
            );
        } catch (Exception e) {
            log.warn("summary parse failed. raw={}", truncate(rawJson), e);
            return new ParsedSummary(
                    "요약 파싱에 실패했습니다. raw=" + truncate(rawJson),
                    null,
                    null,
                    null
            );
        }
    }

    private Integer extractIntSafely(JsonNode node) {
        if (node == null || node.isNull()) return null;
        if (node.isInt()) return clamp(node.asInt());
        if (node.isNumber()) return clamp((int) Math.round(node.asDouble()));
        if (node.isTextual()) {
            String digits = node.asText().replaceAll("[^0-9]", "");
            if (digits.isBlank()) return null;
            return clamp(Integer.parseInt(digits));
        }
        return null;
    }

    private String extractTextSafely(JsonNode node) {
        if (node == null || node.isNull()) return null;
        if (node.isTextual()) return node.asText();
        // 배열/객체로 오면 문자열로 변환 (bullet list가 배열로 오는 케이스 대응)
        return node.toString();
    }

    private Integer clamp(int v) {
        return (v >= 0 && v <= 100) ? v : null;
    }

    private String truncate(String s) {
        if (s == null) return "";
        return s.length() > 500 ? s.substring(0, 500) + "...(truncated)" : s;
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }


    private record ParsedSummary(
            String summaryText,
            Integer totalScore,
            String strengths,
            String improvements
    ) {}
}
