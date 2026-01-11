package com.mockio.ai_service.openAi.generator;

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