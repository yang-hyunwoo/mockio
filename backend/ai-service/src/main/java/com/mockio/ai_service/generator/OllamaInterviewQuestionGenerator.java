package com.mockio.ai_service.generator;

import com.mockio.common_ai_contractor.generator.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OllamaInterviewQuestionGenerator implements InterviewQuestionGenerator {

//    private final WebClient openAiWebClient;
//    private final AiProperties props;
    @Override
    public List<GeneratedQuestion> generate(GenerateQuestionCommand command) {
        // 1) prompt 구성
        // 2) POST /api/generate (stream:false, format:"json")
        // 3) JSON 배열 파싱 -> List<GeneratedQuestion> 반환
        return List.of();
    }
}
