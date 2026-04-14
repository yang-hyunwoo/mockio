package com.mockio.core_service.ai.ollama.generator;

/**
 * Ollama 기반 면접 질문 비교 요약 생성기.
 *
 * Ollama Chat Completion API를 사용하며, 응답은 JSON 스키마로만 출력되도록
 * 강하게 제약된 프롬프트를 적용한다.</p>
 *
 * 그마저 실패할 경우 서비스 연속성을 위해 고정된 폴백 질문을 반환한다.</p>
 *
 * <p>ai.generator=ollama 설정에서만 활성화되며,
 * CompareQuestionGenerator 전략 구현체로 사용된다.</p>
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.compare.*;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.openAi.client.SpringAiOpenAIClient;
import com.mockio.core_service.ai.util.AiResponseSanitizer;
import com.mockio.core_service.ai.util.PromptLoader;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum.ILLEGAL_STATE;

@Component
@RequiredArgsConstructor
@Slf4j
public class OllamaCompareQuestionGenerator implements CompareQuestionGenerator {

    private final SpringAiOpenAIClient client;
    private final PromptLoader promptLoader;
    private final AiResponseSanitizer sanitizer;
    private static final String MODEL = "llama3.1:8b";
    private String commandPrompt;
    private String systemPrompt;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "ai/prompt/compare/";
        commandPrompt = promptLoader.load(absPath + "compare-summary-command-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "compare-summary-prompt-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OLLAMA;
    }

    /**
     * 이전 면접 질문 비교에 대한 요약을 생성한다.
     *
     * <p>처리 흐름:
     * <ol>
     *   <li>Ollama 호출 및 JSON 파싱 시도</li>
     *   <li>최종 실패 시 고정 폴백 질문 반환</li>
     * </ol>
     *
     * @param command 이전 면접 질문 비교에 대한 요약 생성을 위한 입력 정보
     * @return 이전 면접 질문 비교에 대한 요약을 생성한다.
     */
    @Override
    @CircuitBreaker(name = "ollamaCompareQuestionChat")
    public GeneratedCompareQuestion generate(GeneratedCompareQuestionCommand command) {


        String commandText = commandPrompt.formatted(command.track().getLabel());

        String prompt = systemPrompt.formatted(
                command.evaluationCriteria(),
                command.questionTitle(),
                trimAnswer(command.prevAnswer()),
                command.questionTitle(),
                trimAnswer(command.currentAnswer()),
                command.feedbackStyle()
        );


        Double temperature = 0.2;
        String answer = client.chat(MODEL, prompt,commandText,temperature);

        ObjectMapper mapper = new ObjectMapper();
        GeneratedCompareQuestion q;
        try {
            q = mapper.readValue(answer, GeneratedCompareQuestion.class);
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패: {}", answer, e);
            throw new CustomApiException(ILLEGAL_STATE.getHttpStatus(),
                    ILLEGAL_STATE,
                    ILLEGAL_STATE.getMessage());
        }

        return new GeneratedCompareQuestion(
                q.headline(),
                sanitizer.normalizeBody(q.summary()),
                q.strengths(),
                q.improvements(),
                q.improvementTags(),
                q.verdict(),
                "OPENAI",
                MODEL,
                "v1",
                temperature
        );
    }
    
    private String trimAnswer(String answer) {
        if (answer == null) return "";
        return answer.length() > 400 ? answer.substring(0, 400) : answer;
    }

}
