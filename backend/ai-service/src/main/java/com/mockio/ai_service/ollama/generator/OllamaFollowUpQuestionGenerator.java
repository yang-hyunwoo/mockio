package com.mockio.ai_service.ollama.generator;

/**
 * Ollama 기반 후속(팔로업) 질문 생성기.
 *
 * <p>최근 인터뷰 문답을 바탕으로 다음에 이어질 후속 질문 1개를 생성한다.
 * Ollama Chat Completion API를 사용하며, 응답은 JSON 스키마로만 출력되도록
 * 강하게 제약된 프롬프트를 적용한다.</p>
 *
 * <p>응답 파싱 실패에 대비해 1회 리페어(repair) 요청을 수행하고,
 * 그마저 실패할 경우 서비스 연속성을 위해 고정된 폴백 질문을 반환한다.</p>
 *
 * <p>ai.generator=Ollama 설정에서만 활성화되며,
 * FollowUpQuestionGenerator 전략 구현체로 사용된다.</p>
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.ai_service.ollama.client.OllamaClient;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import com.mockio.common_ai_contractor.generator.question.AiQuestion;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Component
@RequiredArgsConstructor
public class OllamaFollowUpQuestionGenerator implements FollowUpQuestionGenerator {

    private static final String MODEL = "llama3.1:8b";
    private static final String PROMPT_VERSION = "followup-v1";

    private final OllamaClient client;

    @Override
    public AiEngine engine() {
        return AiEngine.OLLAMA;
    }

    /**
     * 최근 인터뷰 문답을 기반으로 후속 질문을 생성한다.
     *
     * <p>처리 흐름:
     * <ol>
     *   <li>ollama 호출 및 JSON 파싱 시도</li>
     *   <li>파싱 실패 시 JSON-only 출력 리페어 요청</li>
     *   <li>최종 실패 시 고정 폴백 질문 반환</li>
     * </ol>
     *
     * @param command 후속 질문 생성을 위한 입력 정보
     * @return 생성된 후속 질문 결과
     */
    @Override
    @CircuitBreaker(name = "ollamaFollowUpChat")
    public FollowUpQuestion generate(FollowUpQuestionCommand command) {

        var qa = command.recentQa();
        String qText = (qa == null || qa.question() == null) ? "N/A" : qa.question();
        String aText = (qa == null || qa.answer() == null) ? "" : qa.answer();

        String commandText = """
                당신은 %s 기술 면접관입니다.
                모든 질문은 반드시 한국어로 작성한다.
                영어 문장 사용 금지
                기술 용어만 영어 허용
                반드시 JSON 객체만 출력하세요.
                설명/번호/불릿/마크다운/코드블록 금지.
                """.formatted(command.interviewTrack());


        String prompt = """
                아래 문답을 바탕으로 다음 꼬리질문 1개를 생성하세요.
                
                출력 형식(JSON):
                {
                  "title": "string",
                  "body": "string",
                  "tags": ["string","string"]
                }
                
                규칙:
                - title: 3~8단어의 짧은 주제명
                - body: 1~2문장 이내의 꼬리질문
                - tags: 2~4개, 짧은 기술 키워드
                - “더 설명해보세요” 같은 일반 질문 금지
                - JSON 외 텍스트 절대 금지
                
                조건:
                - 트랙: %s
                - 난이도: %s
                - 꼬리질문 사유: %s
                
                문답:
                Q: %s
                A: %s
                """.formatted(
                command.interviewTrack(),
                command.interviewDifficulty(),
                command.followUpReason(),
                qText,
                aText
        );

        Double temperature = 0.2;
        String answer = client.chat(MODEL, prompt,commandText,temperature);

        ObjectMapper mapper = new ObjectMapper();
        AiQuestion q;
        try {
            q = mapper.readValue(answer, AiQuestion.class);
        } catch (Exception e) {
            // 1회 리페어
            String repairSystem = """
                        반드시 JSON 객체만 출력하세요. 다른 텍스트 금지.
                        스키마:
                        {"title":"string","body":"string","tags":["string","string"]}
                        """;
            String repaired = client.chat(MODEL, "이전 응답을 위 스키마에 맞는 JSON으로만 변환하세요.", repairSystem, temperature);

            try {
                q = mapper.readValue(repaired, AiQuestion.class);
            } catch (Exception e2) {
                // 폴백
                q = new AiQuestion(
                        "추가 검증 질문",
                        "방금 답변에서 가장 중요한 가정(전제)은 무엇이고, 그 전제가 깨질 때 어떤 문제가 발생하나요?",
                        Set.of("Follow-up", "Trade-off")
                );
            }
        }

        return new FollowUpQuestion(new FollowUpQuestion.Item(
                safeTitle(q.title()),
                normalizeBody(q.body())
                ,sanitizeTags(q.tags()),
                "OPENAI",
                MODEL,
                "v1",
                temperature
        ));
    }

    private String normalizeBody(String body) {
        if (body == null) return "";
        String q = body.trim();
        if (q.isBlank()) return q;
        if (!q.endsWith("?") && !q.endsWith("요.") && !q.endsWith(".")) q += "?";
        return q;
    }

    private String safeTitle(String title) {
        if (title == null) return "후속 질문";
        String t = title.trim().replaceAll("^\"|\"$", "").trim();
        return t.isBlank() ? "후속 질문" : (t.length() > 40 ? t.substring(0, 40) : t);
    }

    private Set<String> sanitizeTags(Set<String> tags) {
        if (tags == null) return Set.of();

        return tags.stream()
                .map(String::trim)
                .filter(t -> !t.isBlank())
                .map(t -> t.length() > 20 ? t.substring(0, 20) : t)
                .distinct()
                .limit(4)
                .collect(toSet());
    }

}
