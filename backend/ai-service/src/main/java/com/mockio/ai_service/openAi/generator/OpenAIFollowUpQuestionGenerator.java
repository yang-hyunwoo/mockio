package com.mockio.ai_service.openAi.generator;

/**
 * OpenAI 기반 후속(팔로업) 질문 생성기.
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

import com.mockio.ai_service.openAi.client.OpenAIClient;
import com.mockio.ai_service.util.JsonSupport;
import com.mockio.common_ai_contractor.generator.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.FollowUpQuestionGenerator;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ConditionalOnProperty(name="ai.generator" , havingValue = "openai")
@RequiredArgsConstructor
public class OpenAIFollowUpQuestionGenerator implements FollowUpQuestionGenerator {

    private static final String MODEL = "gpt-4o-mini";
    private static final String PROMPT_VERSION = "followup-v1";

    private final OpenAIClient client;

    /**
     * 최근 인터뷰 문답을 기반으로 후속 질문을 생성한다.
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
    @CircuitBreaker(name = "openaiFollowUpChat", fallbackMethod = "fallbackFollow")
    public FollowUpQuestion generate(FollowUpQuestionCommand command) {

        var qa = command.recentQa();
        String qText = (qa == null || qa.question() == null) ? "N/A" : qa.question();
        String aText = (qa == null || qa.answer() == null) ? "" : qa.answer();

        String prompt = """
        당신은 %s 기술면접관입니다.
        아래 문답을 바탕으로, 다음에 이어질 꼬리질문 1개를 생성해 주세요.

        조건:
        - 트랙: %s
        - 난이도: %s
        - 꼬리질문 사유: %s
        - 질문은 실무 중심으로 작성
        - 1~2문장 이내
        - “더 설명해보세요” 같은 일반 질문 금지
        - 번호/불릿/불필요한 설명 없이 질문 한 줄만 반환

        문답:
        Q: %s
        A: %s
    """.formatted(
                command.interviewTrack(),
                command.interviewTrack(),
                command.interviewDifficulty(),
                command.followUpReason(),
                qText,
                aText
        );

        String answer = client.chat(MODEL, prompt);

        String line = Arrays.stream(answer.split("\n"))
                .map(String::trim)
                .map(s -> s.replaceFirst("^(Q:|질문:)\\s*", "").trim())
                .map(s -> s.replaceFirst("^\\d+\\.|^[-•]\\s*", "").trim())
                .map(s -> s.replaceAll("^\"|\"$", "").trim())
                .filter(s -> !s.isBlank())
                .filter(s -> s.length() >= 10)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("follow-up question parse failed"));

        String normalized = normalizeQuestionLine(line);

        return new FollowUpQuestion(new FollowUpQuestion.Item(
                normalized, "OPENAI", MODEL, "v1", 0.0
        ));
    }


    private String normalizeQuestionLine(String line) {
        String q = line.trim();
        if (!q.endsWith("?") && !q.endsWith("요.") && !q.endsWith(".")) {
            q = q + "?";
        }
        return q;
    }

    @SuppressWarnings("unused")
    private FollowUpQuestion fallbackFollow(FollowUpQuestionCommand command, Throwable t) {
        String q = "방금 답변에서 핵심 근거를 한 문장으로 요약하고, 그 근거가 깨질 수 있는 반례를 하나 들어보세요.";
        return new FollowUpQuestion(new FollowUpQuestion.Item(
                q, "FALLBACK", MODEL, "v1", 0.0
        ));
    }

}
