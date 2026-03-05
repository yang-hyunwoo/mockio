package com.mockio.ai_service.ollama.generator;

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
import com.mockio.ai_service.ollama.client.OllamaClient;
import com.mockio.ai_service.openAi.client.SpringAiOpenAIClient;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveDecision;
import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveGenerator;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.GeneratedDeepDiveBundle;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name="ai.generator" , havingValue = "ollama")
@RequiredArgsConstructor
public class OllamaAIDeepDiveGenerator implements DeepDiveGenerator {

    private final ObjectMapper objectMapper;
    private static final String MODEL = "llama3.1:8b";
    private static final String PROMPT_VERSION = "followup-v1";

    private final OllamaClient client;

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
    @CircuitBreaker(name = "ollamaDeepDiveChat")
    public GeneratedDeepDiveBundle generate(GenerateDeepDiveCommand command) {

        String qText = (command == null || command.question() == null) ? "N/A" : command.question();
        String aText = (command == null || command.answer() == null) ? "" : command.answer();

        String commandText = """
                        당신은 %s 기술 면접 보조 시스템입니다.
                        반드시 JSON만 출력하세요.
                        설명/마크다운/코드블록/번호/불릿 금지.
                        스키마를 절대 깨지 마세요.
                        """.formatted(command.interviewTrack());
        Double temperature = 0.2;

        String prompt = """
                당신은 기술 면접 보조 시스템입니다.
                먼저 딥다이브 필요 여부를 판단하고,
                필요한 경우에만 질문을 1개 생성하십시오.
                반드시 JSON만 출력하십시오.
                판단이 애매하면 shouldFollowUp=false로 하십시오.
        
                아래 스키마에 맞는 JSON만 반환하십시오.
        
                {
                  "decision": {
                    "shouldFollowUp": boolean,
                    "depth": integer,           // 1..3
                    "focus": string[],          // 0..3
                    "gaps": string[],           // 0..3
                    "reason": string            // 한 문장
                  },
                  "question": {
                      "title": "string",
                      "body": "string",
                      "tags": ["string","string"]
                  } | null
                }
                
                조건:
                - 트랙: %s
                - 난이도: %s
                
                규칙:
                - question.body는 1~2문장 이내
                - “더 설명해보세요” 같은 일반 질문 금지
                - tags는 2~4개, 짧은 기술 키워드
                - JSON 외의 텍스트는 절대 출력하지 마세요.
        
                문답:
                Q: %s
                A: %s
                """.formatted(
                        command.interviewTrack(),
                        command.interviewDifficulty(),
                        qText,
                        aText
                );
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

        String normalizedBody = normalizeQuestionLine(dto.question().body());

        FollowUpQuestion question = new FollowUpQuestion(
                new FollowUpQuestion.Item(
                        safeTitle(dto.question().title()),
                        normalizedBody,
                        sanitizeTags(dto.question().tags()),
                        "OPENAI",
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

    private List<String> sanitizeTags(List<String> tags) {
        if (tags == null) return List.of();

        return tags.stream()
                .map(String::trim)
                .filter(t -> !t.isBlank())
                .map(t -> t.length() > 20 ? t.substring(0, 20) : t)
                .distinct()
                .limit(4)
                .toList();
    }

    private DeepDiveBundleDto parseOrRepair(String raw, String system) {
        try {
            return objectMapper.readValue(raw, DeepDiveBundleDto.class);
        } catch (Exception first) {
            // repair 1회

            String repairPrompt = """
            아래 텍스트를 '오직 JSON'으로만 변환하십시오.
            스키마를 반드시 지키고, 누락 필드는 기본값으로 채우십시오.
            추가 텍스트/설명/마크다운 금지.

            스키마:
            {
              "decision": {
                "shouldFollowUp": boolean,
                "depth": integer,
                "focus": string[],
                "gaps": string[],
                "reason": string
              },
            "question": {
                      "title": "string",
                      "body": "string",
                      "tags": ["string","string"]
                  } | null
            }

            원문:
            %s
            """.formatted(raw);

            String repaired = client.chat(MODEL, repairPrompt, system, 0.0);

            try {
                return objectMapper.readValue(repaired, DeepDiveBundleDto.class);
            } catch (Exception second) {
                // 최종 폴백: 안전하게 false
                return DeepDiveBundleDto.fallback();
            }
        }
    }

    private String normalizeQuestionLine(String line) {
        String q = line == null ? "" : line.trim();
        q = q.replaceAll("^\"|\"$", "").trim();
        if (!q.endsWith("?") && !q.endsWith("요.") && !q.endsWith(".")) q = q + "?";
        return q;
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
        if (xs == null) return List.of();
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

        public record Question(String title, String body, List<String> tags) {}

        public static DeepDiveBundleDto fallback() {
            return new DeepDiveBundleDto(
                    new Decision(false, 1, List.of(), List.of(), "FALLBACK"),
                    null
            );
        }
    }

}
