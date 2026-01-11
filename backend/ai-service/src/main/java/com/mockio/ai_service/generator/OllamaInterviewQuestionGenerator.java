package com.mockio.ai_service.generator;

/**
 * Ollama 기반 로컬 LLM을 사용하여 인터뷰 질문을 생성하는 Generator 구현체.
 *
 * <p>GenerateQuestionCommand를 기반으로 프롬프트를 구성한 뒤
 * Ollama의 /api/generate 엔드포인트를 호출하여 JSON 형식의
 * 질문 목록을 생성한다.</p>
 *
 * <p>외부 SaaS(OpenAI) 의존성을 줄이기 위한 대체/폴백 전략으로 사용되며,
 * 네트워크 비용 없이 로컬 환경에서 동작하는 것이 특징이다.</p>
 */

import com.mockio.common_ai_contractor.generator.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OllamaInterviewQuestionGenerator implements InterviewQuestionGenerator {

//    private final WebClient openAiWebClient;
//    private final AiProperties props;
    @Override
    public GeneratedQuestion generate(GenerateQuestionCommand command) {
        // 1) prompt 구성
        // 2) POST /api/generate (stream:false, format:"json")
        // 3) JSON 배열 파싱 -> List<GeneratedQuestion> 반환
        return null;
    }
}
