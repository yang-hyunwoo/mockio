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
import com.mockio.common_ai_contractor.constant.AiEngine;
import com.mockio.common_ai_contractor.generator.question.AiQuestion;
import com.mockio.common_ai_contractor.generator.question.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.question.InterviewQuestionGenerator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaInterviewQuestionGenerator implements InterviewQuestionGenerator {

    private final OllamaClient client;
    private final String model = "llama3.1:8b";


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
        String commandText = """
                당신은 %s 기술 면접관입니다.
                title, body, tags를 모두 한국어로 작성하세요.
                영어 문장 사용 금지, 기술 용어만 영어 허용.
                반드시 JSON 배열만 출력하세요.
                설명, 코드블록, 마크다운, 번호 금지.
                """.formatted(command.track());

        String prompt = """
            다음 조건으로 면접 질문을 %d개 생성하세요.
                
            출력 형식(JSON):
                    [
                      {
                        "title": "string",
                        "body": "string",
                        "tags": ["string","string"]
                      }
                    ]
            규칙:
                    - title은 3~8단어의 짧은 주제명
                    - body는 실제 면접 질문 문장
                    - tags는 2~4개, 짧은 기술 키워드
                    - JSON 외의 텍스트는 절대 출력하지 마세요
           조건:
           - 면접 질문 분야: %s
           - 난이도: %s
           - 질문은 실무 중심으로 작성
        """.formatted(
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
                    sanitizeTags(q.tags()),
                    "ollama",
                    "ollama",
                    "v1",
                    temperature
            ));
        }

       return new GeneratedQuestion(result);
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
