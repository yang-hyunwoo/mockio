package com.mockio.ai_service.generator;

import com.mockio.common_ai_contractor.generator.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FakeInterviewQuestionGenerator implements InterviewQuestionGenerator {

    @Override
    public GeneratedQuestion generate(GenerateQuestionCommand command) {
        int n = Math.max(1, command.questionCount());

        List<String> pool = List.of(
                "Spring Bean 생명주기(생성~소멸)와 각 단계에서 개입할 수 있는 방법을 설명해보세요.",
                "JWT 인증/인가 흐름을 설명하고, Access/Refresh 토큰을 분리하는 이유를 말해보세요.",
                "트랜잭션 전파(Propagation) 옵션 중 REQUIRED와 REQUIRES_NEW의 차이를 예시로 설명해보세요.",
                "JPA 영속성 컨텍스트의 1차 캐시와 Dirty Checking이 동작하는 원리를 설명해보세요.",
                "동시성 문제를 해결하기 위한 낙관락/비관락의 차이를 말하고, 각각의 적용 사례를 들어보세요.",
                "인덱스가 왜 성능에 영향을 주는지, B-Tree 인덱스 기준으로 설명해보세요.",
                "N+1 문제의 원인과 해결 방법(Fetch Join, EntityGraph 등)을 비교해보세요.",
                "Redis를 캐시로 쓸 때 캐시 스탬피드/캐시 무효화 전략을 어떻게 가져갈지 설명해보세요."
        );

        List<GeneratedQuestion.Item> result = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            String text = pool.get((i - 1) % pool.size());
            result.add(new GeneratedQuestion.Item(
                    i,
                    "[" + command.track() + "/" + command.difficulty() + "] " + text,
                    "FAKE",
                    "fake-model",
                    "v0",
                    0.0
            ));
        }
        return new GeneratedQuestion(result);
    }
}
