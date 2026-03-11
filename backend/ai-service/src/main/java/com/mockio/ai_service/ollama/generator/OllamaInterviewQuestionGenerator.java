package com.mockio.ai_service.ollama.generator;

/**
 * Ollama 기반 인터뷰 질문 생성기 구현체.
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
import com.mockio.ai_service.ollama.client.OllamaClient;
import com.mockio.ai_service.util.AiResponseSanitizer;
import com.mockio.ai_service.util.PromptLoader;
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.question.AiQuestion;
import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.question.InterviewQuestionGenerator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaInterviewQuestionGenerator implements InterviewQuestionGenerator {

    private final OllamaClient client;
    private final PromptLoader promptLoader;
    private final AiResponseSanitizer sanitizer;
    private static final String model = "llama3.1:8b";
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
        return AiEngine.OLLAMA;
    }

    /**
     * 인터뷰 질문 생성 요청을 처리한다.
     *
     * <p>Ollama 응답을 줄 단위로 분리한 뒤,
     * 질문 번호 및 불필요한 접두어를 제거하고
     * 요청된 개수만큼 질문을 선별하여 반환한다.</p>
     *
     * @param command 질문 생성에 필요한 입력 정보
     * @return 생성된 인터뷰 질문 목록
     */
    @Override
    @CircuitBreaker(name = "ollamaChat")
    public GeneratedQuestion generate(GenerateQuestionCommand command) {
        String commandText = commandPrompt.formatted(command.track());

        String prompt = systemPrompt.formatted(
                command.questionCount(),
                command.track(),
                command.difficulty()
        );
        Double temperature = 0.7;
        String answer = client.chat(model, prompt, commandText, temperature);

        ObjectMapper mapper = new ObjectMapper();

        List<AiQuestion> aiQuestions;
        try {
            aiQuestions = mapper.readValue(answer, new TypeReference<List<AiQuestion>>() {
            });
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패: {}", answer, e);
            throw new RuntimeException("AI 응답 파싱 실패");
        }
        List<GeneratedQuestion.Item> result = new ArrayList<>();
        for (int i = 0; i < aiQuestions.size(); i++) {
            AiQuestion q = aiQuestions.get(i);

            result.add(new GeneratedQuestion.Item(
                    ((i + 1) * 10),
                    q.title(),
                    q.body(),
                    sanitizer.sanitizeTags(q.tags()),
                    "ollama",
                    "ollama",
                    "v1",
                    temperature
            ));
        }
       return new GeneratedQuestion(result);
    }

}
