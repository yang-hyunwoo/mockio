package com.mockio.ai_service.openAi.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mockio.ai_service.openAi.client.SpringAiOpenAIClient;
import com.mockio.ai_service.util.AiResponseSanitizer;
import com.mockio.ai_service.util.PromptLoader;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedback;
import com.mockio.common_ai_contractor.generator.feedback.GeneratedSummaryFeedbackCommand;
import com.mockio.common_ai_contractor.generator.feedback.SummaryFeedbackGenerator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAISummaryFeedbackGenerator implements SummaryFeedbackGenerator {

    private final SpringAiOpenAIClient client;
    private final ObjectMapper objectMapper;
    private final PromptLoader promptLoader;
    private final AiResponseSanitizer sanitizer;
    private static final String MODEL = "gpt-4o-mini";
    private static final String PROVIDER = "OPENAI";
    private String commandPrompt;
    private String commandPromptRepair;
    private String systemRepairPrompt;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "ai/prompt/summary/";
        commandPrompt = promptLoader.load(absPath + "summary-command-prompt-" + promptVersion + ".txt");
        commandPromptRepair = promptLoader.load(absPath + "summary-command-prompt-repair-" + promptVersion + ".txt");
        systemRepairPrompt = promptLoader.load(absPath + "summary-prompt-repair-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    @Override
    @CircuitBreaker(name = "openaiSummaryChat")
    public GeneratedSummaryFeedback generate(GeneratedSummaryFeedbackCommand command) {

        String commandText = commandPrompt.formatted(command.track(), command.difficulty(), command.feedbackStyle());

        String prompt = buildUser(command);

        Double temperature = 0.2;

        String rawJson = client.chat(MODEL, prompt, commandText, temperature);

        if (!isValidSummaryFeedbackJson(rawJson)) {
            log.warn("summary feedback json invalid. try repair. raw={}", sanitizer.truncate(rawJson));
            rawJson = repairSummaryFeedback(rawJson, temperature);
        }

        if (!isValidSummaryFeedbackJson(rawJson)) {
            log.warn("summary feedback repair failed. use fallback. raw={}", sanitizer.truncate(rawJson));
            rawJson = buildFallbackSummaryFeedbackJson();
        }

        Integer score = sanitizer.extractScoreSafely(rawJson,"totalScore");

        return new GeneratedSummaryFeedback(
                command.interviewId(),
                rawJson,
                score,
                PROVIDER,
                MODEL,
                promptVersion,
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

        sb.append("평가 참고 메모:\n")
                .append("- 답변 수: ").append(command.items().size()).append("\n")
                .append("- 각 답변을 읽고 반복적으로 나타나는 장점과 약점을 중심으로 총평을 작성하세요.\n")
                .append("- 개별 질문 하나의 특이점보다 전체적인 패턴을 우선 반영하세요.\n\n");

        sb.append("요약 작성 지침:\n")
                .append("- 반복적으로 드러난 장점과 약점을 우선 반영하세요.\n")
                .append("- 한 질문에서만 나타난 특이점보다 여러 답변에서 공통으로 보인 특징을 더 중요하게 평가하세요.\n")
                .append("- 총평은 전체 인터뷰를 대표하는 내용만 포함하세요.\n\n");

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

    private String repairSummaryFeedback(String invalidRawJson, Double temperature) {
        String repairSystem = commandPromptRepair;

        String repairUser = systemRepairPrompt.formatted(nullToEmpty(invalidRawJson));

        try {
            return client.chat(MODEL, repairUser, repairSystem, temperature);
        } catch (Exception e) {
            log.warn("summary repair call failed. raw={}", sanitizer.truncate(invalidRawJson), e);
            return invalidRawJson;
        }
    }

    private boolean isValidSummaryFeedbackJson(String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);

            JsonNode totalScore = root.get("totalScore");
            JsonNode summaryText = root.get("summaryText");
            JsonNode strengths = root.get("strengths");
            JsonNode improvements = root.get("improvements");

            if (totalScore == null || totalScore.isNull()) return false;
            if (sanitizer.extractScoreSafely(rawJson,"totalScore") == null) return false;

            if (summaryText == null || !summaryText.isTextual() || summaryText.asText().isBlank()) {
                return false;
            }

            if (strengths == null || !strengths.isArray()) return false;
            if (improvements == null || !improvements.isArray()) return false;

            for (JsonNode item : strengths) {
                if (!item.isTextual() || item.asText().isBlank()) return false;
            }

            for (JsonNode item : improvements) {
                if (!item.isTextual() || item.asText().isBlank()) return false;
            }
            return true;
        } catch (Exception e) {
            log.warn("summary feedback json validation failed. raw={}", sanitizer.truncate(rawJson), e);
            return false;
        }
    }

    private String buildFallbackSummaryFeedbackJson() {
        try {
            ObjectNode root = objectMapper.createObjectNode();
            root.put("totalScore", 50);
            root.put("summaryText",
                    "전체적으로 답변의 방향성은 보였지만, 구체적인 사례와 설명이 충분히 드러나지 않아 강점과 개선점을 더 분명하게 파악하기는 어려웠습니다. "
                            + "질문의 핵심을 벗어나지는 않았지만, 실제 경험을 바탕으로 한 설명이 조금 더 보완되면 좋겠습니다. "
                            + "특히 문제 해결 과정과 판단 기준을 함께 설명해 주시면 답변의 설득력이 더 높아질 수 있습니다. "
                            + "다음에는 경험 중심으로 조금 더 구체적으로 답변해 주시면 더 좋은 평가로 이어질 수 있겠습니다."
            );

            ArrayNode strengths = root.putArray("strengths");
            strengths.add("답변에 성실하게 임하려는 태도가 보였습니다.");

            ArrayNode improvements = root.putArray("improvements");
            improvements.add("실제 경험이나 사례를 중심으로 설명해 주시면 좋겠습니다.");
            improvements.add("문제 해결 과정과 판단 기준을 조금 더 구체적으로 들려주시면 좋겠습니다.");

            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            log.error("fallback summary feedback json build failed", e);
            return """
                {
                  "totalScore": 50,
                  "summaryText": "전체적으로 답변의 방향성은 보였지만, 구체적인 사례와 설명이 충분히 드러나지 않아 강점과 개선점을 더 분명하게 파악하기는 어려웠습니다. 질문의 핵심을 벗어나지는 않았지만, 실제 경험을 바탕으로 한 설명이 조금 더 보완되면 좋겠습니다. 특히 문제 해결 과정과 판단 기준을 함께 설명해 주시면 답변의 설득력이 더 높아질 수 있습니다. 다음에는 경험 중심으로 조금 더 구체적으로 답변해 주시면 더 좋은 평가로 이어질 수 있겠습니다.",
                  "strengths": ["답변에 성실하게 임하려는 태도가 보였습니다."],
                  "improvements": [
                    "실제 경험이나 사례를 중심으로 설명해 주시면 좋겠습니다.",
                    "문제 해결 과정과 판단 기준을 조금 더 구체적으로 들려주시면 좋겠습니다."
                  ]
                }
                """;
        }
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

}
