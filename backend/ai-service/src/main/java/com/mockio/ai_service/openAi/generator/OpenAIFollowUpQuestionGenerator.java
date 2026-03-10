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


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.ai_service.openAi.client.SpringAiOpenAIClient;
import com.mockio.ai_service.util.AiResponseSanitizer;
import com.mockio.ai_service.util.PromptLoader;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionCommand;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestion;
import com.mockio.common_ai_contractor.generator.followup.FollowUpQuestionGenerator;
import com.mockio.common_ai_contractor.generator.question.AiQuestion;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnProperty(name="ai.generator" , havingValue = "openai")
@RequiredArgsConstructor
public class OpenAIFollowUpQuestionGenerator implements FollowUpQuestionGenerator {

    private final SpringAiOpenAIClient client;
    private final PromptLoader promptLoader;
    private final AiResponseSanitizer sanitizer;
    private static final String MODEL = "gpt-4o-mini";
    private String commandPrompt;
    private String systemPrompt;
    private String systemRepairPrompt;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "prompt/followup/";
        commandPrompt = promptLoader.load(absPath + "followup-command-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "followup-prompt-" + promptVersion + ".txt");
        systemRepairPrompt = promptLoader.load(absPath + "followup-prompt-repair-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

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
    @CircuitBreaker(name = "openaiFollowUpChat")
    public FollowUpQuestion generate(FollowUpQuestionCommand command) {

        var qa = command.recentQa();
        String qText = (qa == null || qa.question() == null) ? "N/A" : qa.question();
        String aText = (qa == null || qa.answer() == null) ? "" : qa.answer();

        String commandText = commandPrompt.formatted(command.interviewTrack());

        String prompt = systemPrompt.formatted(
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
                String repairSystem = systemRepairPrompt;
                String repaired = client.chat(MODEL, "이전 응답을 위 스키마에 맞는 JSON으로만 변환하세요.", repairSystem, temperature);

                try {
                    q = mapper.readValue(repaired, AiQuestion.class);
                } catch (Exception e2) {
                    // 폴백
                    q = new AiQuestion(
                            "추가 검증 질문",
                            "방금 답변에서 가장 중요한 가정(전제)은 무엇이고, 그 전제가 깨질 때 어떤 문제가 발생하나요?",
                            Set.of("검증", "근거")
                    );
                }
            }

        return new FollowUpQuestion(new FollowUpQuestion.Item(
                safeTitle(q.title()),
                sanitizer.normalizeBody(q.body())
                ,sanitizer.sanitizeTags(q.tags()),
                "OPENAI",
                MODEL,
                "v1",
                temperature
        ));
    }

    private String safeTitle(String title) {
        if (title == null) return "후속 질문";
        String t = title.trim().replaceAll("^\"|\"$", "").trim();
        return t.isBlank() ? "후속 질문" : (t.length() > 40 ? t.substring(0, 40) : t);
    }

}
