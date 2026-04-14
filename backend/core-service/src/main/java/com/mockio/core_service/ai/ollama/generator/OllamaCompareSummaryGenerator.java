package com.mockio.core_service.ai.ollama.generator;

/**
 * OpenAI 기반 면접 비교 요약 생성기.
 *
 * OpenAI Chat Completion API를 사용하며, 응답은 JSON 스키마로만 출력되도록
 * 강하게 제약된 프롬프트를 적용한다.</p>
 *
 * <p>응답 파싱 실패에 대비해 1회 리페어(repair) 요청을 수행하고,
 * 그마저 실패할 경우 서비스 연속성을 위해 고정된 폴백 질문을 반환한다.</p>
 *
 * <p>ai.generator=openai 설정에서만 활성화되며,
 * CompareSummaryGenerator 전략 구현체로 사용된다.</p>
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.compare.CompareSummaryGenerator;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummary;
import com.mockio.common_ai_contractor.generator.compare.GeneratedCompareSummaryCommand;
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
public class OllamaCompareSummaryGenerator implements CompareSummaryGenerator {

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
    @CircuitBreaker(name = "ollamaCompareSummaryChat")
    public GeneratedCompareSummary generate(GeneratedCompareSummaryCommand command) {


        String commandText = commandPrompt.formatted(command.track().getLabel());

        GeneratedCompareSummaryCommand.Interview prevInterview = command.prevInterview();
        GeneratedCompareSummaryCommand.Interview currentInterview = command.currentInterview();
        String prompt = systemPrompt.formatted(
                prevInterview.totalScore(),
                prevInterview.feedbackDimensions().structure(),
                prevInterview.feedbackDimensions().clarity(),
                prevInterview.feedbackDimensions().specificity(),
                prevInterview.feedbackJobMetric().practicality(),
                prevInterview.feedbackJobMetric().decisionMaking(),
                prevInterview.feedbackJobMetric().tradeoff(),
                prevInterview.summary(),
                currentInterview.totalScore(),
                currentInterview.feedbackDimensions().structure(),
                currentInterview.feedbackDimensions().clarity(),
                currentInterview.feedbackDimensions().specificity(),
                currentInterview.feedbackJobMetric().practicality(),
                currentInterview.feedbackJobMetric().decisionMaking(),
                currentInterview.feedbackJobMetric().tradeoff(),
                currentInterview.summary(),
                command.totalCount(),
                command.betterCount(),
                command.notCount(),
                buildRepresentativeQuestionText(command.question()),
                command.feedbackStyle()
        );

        Double temperature = 0.2;
        String answer = client.chat(MODEL, prompt,commandText,temperature);

        ObjectMapper mapper = new ObjectMapper();
        GeneratedCompareSummary q;
            try {
                q = mapper.readValue(answer, GeneratedCompareSummary.class);
            } catch (Exception e) {
                log.error("AI 응답 파싱 실패: {}", answer, e);
                throw new CustomApiException(ILLEGAL_STATE.getHttpStatus(),
                        ILLEGAL_STATE,
                        ILLEGAL_STATE.getMessage());
            }

        return new GeneratedCompareSummary(
                q.headline(),
                sanitizer.normalizeBody(q.summary()),
                q.strengths(),
                q.improvements(),
                q.improvementTags(),
                "OPENAI",
                MODEL,
                "v1",
                temperature

        );
    }


    private String buildRepresentativeQuestionText(List<GeneratedCompareSummaryCommand.Item> items) {

        if (items == null || items.isEmpty()) {
            return "없음";
        }

        StringBuilder sb = new StringBuilder();

        for (GeneratedCompareSummaryCommand.Item item : items) {

            sb.append("- (")
                    .append(mapType(item.type()))
                    .append(") \"")
                    .append(item.title())
                    .append("\"\n");

            sb.append("  이전: ")
                    .append(trimAnswer(item.previousAnswer()))
                    .append("\n");

            sb.append("  현재: ")
                    .append(trimAnswer(item.currentAnswer()))
                    .append("\n");

            sb.append("  점수 변화: ")
                    .append(item.previousScore())
                    .append(" → ")
                    .append(item.currentScore())
                    .append(" (")
                    .append(item.delta() > 0 ? "+" : "")
                    .append(item.delta())
                    .append(")\n\n");
        }

        return sb.toString();
    }

    private String mapType(String type) {
        return switch (type) {
            case "MOST_IMPROVED" -> "가장 크게 개선된 질문";
            case "WEAKEST_CURRENT" -> "현재 가장 부족한 질문";
            case "REPRESENTATIVE_BASIC" -> "대표적인 질문";
            default -> type;
        };
    }

    private String trimAnswer(String answer) {
        if (answer == null) return "";
        return answer.length() > 400 ? answer.substring(0, 400) : answer;
    }

}
