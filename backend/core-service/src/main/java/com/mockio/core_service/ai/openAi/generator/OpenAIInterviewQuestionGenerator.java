package com.mockio.core_service.ai.openAi.generator;

/**
 * OpenAI 기반 인터뷰 질문 생성기 구현체.
 *
 * <p>GenerateQuestionCommand를 기반으로 면접 질문 생성을 위한 프롬프트를 구성하고,
 * OpenAI Chat Completion API를 호출하여 질문 목록을 생성한다.</p>
 *
 * <p>응답 결과는 한 줄에 질문 하나 형태로 정규화되며,
 * 번호/불필요한 기호 제거, 중복 제거, 길이 필터링 등을 통해
 * 최종 질문 목록으로 가공된다.</p>
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.question.*;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.openAi.client.SpringAiOpenAIClient;
import com.mockio.core_service.ai.util.AiResponseSanitizer;
import com.mockio.core_service.ai.util.PromptLoader;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIInterviewQuestionGenerator implements InterviewQuestionGenerator {

    private final SpringAiOpenAIClient client;
    private final AiResponseSanitizer sanitizer;
    private final PromptLoader promptLoader;
    private final String MODEL = "gpt-4o-mini";
    private String commandPrompt;
    private String systemPrompt;

    @Value("${ai.prompt-version}")
    private String promptVersion;

    @PostConstruct
    void init() {
        String absPath = "ai/prompt/question/";
        commandPrompt = promptLoader.load(absPath + "question-command-prompt-" + promptVersion + ".txt");
        systemPrompt = promptLoader.load(absPath + "question-prompt-" + promptVersion + ".txt");
    }

    @Override
    public AiEngine engine() {
        return AiEngine.OPENAI;
    }

    /**
     * 인터뷰 질문 생성 요청을 처리한다.
     *
     * <p>OpenAI 응답을 줄 단위로 분리한 뒤,
     * 질문 번호 및 불필요한 접두어를 제거하고
     * 요청된 개수만큼 질문을 선별하여 반환한다.</p>
     *
     * @param command 질문 생성에 필요한 입력 정보
     * @return 생성된 인터뷰 질문 목록
     */
    @Override
    @CircuitBreaker(name = "openaiChat")
    public GeneratedQuestion generate(GenerateQuestionCommand command) {

        String commandText = commandPrompt.formatted(command.track());

        String keywordText = command.interviewKeyword() == null || command.interviewKeyword().isEmpty()
                ? "없음"
                : String.join(", ", command.interviewKeyword());

        String prompt = systemPrompt.formatted(
                command.questionCount(),
                command.track().getLabel(),
                command.difficulty(),
                keywordText
        );
        Double temperature = 0.7;
        String answer = client.chat(MODEL, prompt, commandText, temperature);

        ObjectMapper mapper = new ObjectMapper();

        List<AiQuestion> aiQuestionList;
        try {
            aiQuestionList = mapper.readValue(answer, new TypeReference<List<AiQuestion>>() {
            });
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패: {}", answer, e);
            throw new CustomApiException(ILLEGAL_STATE.getHttpStatus(),
                    ILLEGAL_STATE,
                    ILLEGAL_STATE.getMessage());
        }

        List<GeneratedQuestion.Item> result = new ArrayList<>();
        for (int i = 0; i < aiQuestionList.size(); i++) {
            AiQuestion aiQuestion = aiQuestionList.get(i);
            result.add(new GeneratedQuestion.Item(
                    aiQuestion.basicQuestion(),
                    aiQuestion.hardQuestion(),
                    aiQuestion.primaryTag(),
                    sanitizer.sanitizeTags(aiQuestion.tags()),
                    "OPENAI",
                    MODEL,
                    "v1",
                    temperature
            ));

        }
       return new GeneratedQuestion(result);
    }

}
