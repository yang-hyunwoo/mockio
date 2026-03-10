package com.mockio.ai_service.openAi.generator;

/**
 * OpenAI 기반 후속 질문 생성기.
 *
 * <p>최근 인터뷰 문답을 바탕으로 다음에 이어질 후속 질문 1개를 생성한다.
 * OpenAI Chat Completion API를 사용하며, 응답은 JSON 스키마로만 출력되도록
 * 강하게 제약된 프롬프트를 적용한다.</p>
 *
 * <p>응답 파싱 실패에 대비해 1회 리페어(repair) 요청을 수행하고,
 * 그마저 실패할 경우 서비스 연속성을 위해 고정된 폴백 질문을 반환한다.</p>
 *
 * <p>ai.generator=openai 설정에서만 활성화되며,
 * FollowUpQuestionGenerator 전략 구현체로 사용된다.</p>
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.ai_service.openAi.client.SpringAiOpenAIClient;
import com.mockio.ai_service.util.AiResponseSanitizer;
import com.mockio.ai_service.util.PromptLoader;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.*;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name="ai.generator" , havingValue = "openai")
@RequiredArgsConstructor
public class OpenAIDeepDiveGenerator implements DeepDiveGenerator {

    private final ObjectMapper objectMapper;
    private final PromptLoader promptLoader;
    private final SpringAiOpenAIClient client;
    private final AiResponseSanitizer sanitizer;
    private static final String MODEL = "gpt-4o-mini";
    private static final String PROMPT_VERSION = "followup-v1";
    private String commandPrompt;
    private String systemPrompt;
    private String systemRepairPrompt;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "prompt/deepdive/";
        commandPrompt = promptLoader.load(absPath + "deep-dive-command-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "deep-dive-prompt-" + promptVersion + ".txt");
        systemRepairPrompt = promptLoader.load(absPath + "deep-dive-prompt-repair-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    /**
     * 최근 인터뷰 문답을 기반으로 심화 질문을 생성한다.
     *
     * <p>처리 흐름:
     * <ol>
     *   <li>OpenAI 호출 및 JSON 파싱 시도</li>
     *   <li>파싱 실패 시 JSON-only 출력 리페어 요청</li>
     *   <li>최종 실패 시 고정 폴백 질문 반환</li>
     * </ol>
     *
     * @param command 후속 질문 생성을 위한 입력 정보
     * @return 생성된 후속 질문 결과
     */
    @Override
    @CircuitBreaker(name = "openaiDeepDiveChat")
    public GeneratedDeepDiveBundle generate(GenerateDeepDiveCommand command) {

        String qText = (command == null || command.question() == null) ? "N/A" : command.question();
        String aText = (command == null || command.answer() == null) ? "" : command.answer();

        String commandText = commandPrompt.formatted(command.interviewTrack());

        String prompt = systemPrompt.formatted(command.interviewTrack(),
                        command.interviewTrack(),
                        command.interviewDifficulty(),
                        qText,
                        aText
                );

        Double temperature = 0.2;
        String answer = client.chat(MODEL, prompt,commandText,temperature);
        DeepDiveBundleDto dto = parseOrRepair(answer, commandText);

        DeepDiveDecision decision = new DeepDiveDecision(
                dto.decision().shouldFollowUp(),
                clamp(dto.decision().depth(), 1, 3),
                safeTop3(dto.decision().focus()),
                safeTop3(dto.decision().gaps()),
                safeStr(dto.decision().reason())
        );

        if (!decision.shouldFollowUp() || dto.question() == null || isBlank(dto.question().body())) {
            return new GeneratedDeepDiveBundle(decision, null);
        }

        String normalizedBody = sanitizer.normalizeQuestionLine(dto.question().body());

        FollowUpQuestion question = new FollowUpQuestion(
                new FollowUpQuestion.Item(
                        safeTitle(dto.question().title()),
                        normalizedBody,
                        sanitizer.sanitizeTags(dto.question().tags()),
                        "ollama",
                        MODEL,
                        PROMPT_VERSION,
                        temperature
                )
        );
        return new GeneratedDeepDiveBundle(decision, question);
    }

    private String safeTitle(String title) {
        if (title == null) return "후속 질문";
        String t = title.trim().replaceAll("^\"|\"$", "").trim();
        return t.isBlank() ? "후속 질문" : (t.length() > 40 ? t.substring(0, 40) : t);
    }

    private DeepDiveBundleDto parseOrRepair(String raw, String system) {
        try {
            return objectMapper.readValue(raw, DeepDiveBundleDto.class);
        } catch (Exception first) {
            // repair 1회

            String repairPrompt = systemRepairPrompt.formatted(raw);

            String repaired = client.chat(MODEL, repairPrompt, system, 0.0);

            try {
                return objectMapper.readValue(repaired, DeepDiveBundleDto.class);
            } catch (Exception second) {
                // 최종 폴백: 안전하게 false
                return DeepDiveBundleDto.fallback();
            }
        }
    }


    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safeStr(String s) {
        return s == null ? "" : s.trim();
    }

    private static List<String> safeTop3(List<String> xs) {
        if (xs == null) return java.util.List.of();
        return xs.stream()
                .filter(x -> x != null && !x.trim().isEmpty())
                .map(String::trim)
                .limit(3)
                .toList();
    }

    public record DeepDiveBundleDto(Decision decision, Question question) {
        public record Decision(boolean shouldFollowUp, int depth,
                               List<String> focus,
                               List<String> gaps,
                               String reason) {}

        public record Question(String title, String body, java.util.Set<String> tags) {}

        public static DeepDiveBundleDto fallback() {
            return new DeepDiveBundleDto(
                    new Decision(false, 1, java.util.List.of(), java.util.List.of(), "FALLBACK"),
                    null
            );
        }
    }

}
