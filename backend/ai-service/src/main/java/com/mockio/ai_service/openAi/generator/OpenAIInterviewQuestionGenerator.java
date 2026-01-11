package com.mockio.ai_service.openAi.generator;

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

import com.mockio.ai_service.openAi.client.OpenAIClient;
import com.mockio.common_ai_contractor.generator.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenAIInterviewQuestionGenerator implements InterviewQuestionGenerator {

    private final OpenAIClient client;
    private final String model = "gpt-4o-mini";

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
    public GeneratedQuestion generate(GenerateQuestionCommand command) {

        String prompt = """
            %s 면접 질문을 %d개 생성해 주세요.
           조건:
           - 면접 질문 분야: %s
           - 난이도: %s
           - 질문은 실무 중심으로 작성
           - 한 줄에 질문 하나
           - 번호나 불필요한 설명 없이 질문만 반환
        """.formatted(
                command.track(),
                command.questionCount(),
                command.track(),
                command.difficulty()
        );

        String answer = client.chat(model, prompt);

        List<String> lines = Arrays.stream(answer.split("\n"))
                .map(String::trim)
                .map(s -> s.replaceFirst("^\\d+\\.|^[-•]\\s*", "").trim())
                .filter(s -> !s.isBlank())
                .filter(s -> s.length() >= 10)
                .distinct()
                .limit(command.questionCount())
                .toList();
        List<GeneratedQuestion.Item> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            result.add(new GeneratedQuestion.Item(((i + 1) * 10),
                    lines.get(i).trim(),
                    "OPENAI",
                    model,
                    "v1",
                    0.0));
        }

       return new GeneratedQuestion(result);

    }
}
