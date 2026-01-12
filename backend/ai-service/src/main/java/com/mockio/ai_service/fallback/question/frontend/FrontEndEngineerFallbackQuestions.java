package com.mockio.ai_service.fallback.question.frontend;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FrontEndEngineerFallbackQuestions {

    private FrontEndEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<String>> byDifficulty() {
        Map<InterviewDifficulty, List<String>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                "SPA란 무엇인가요?",
                "HTML과 CSS의 역할 차이는 무엇인가요?",
                "브라우저 렌더링 과정은 어떻게 되나요?",
                "이벤트 버블링이란 무엇인가요?",
                "상태(state)란 무엇인가요?",
                "CSR과 SSR의 차이는 무엇인가요?",
                "API 호출 시 에러 처리는 왜 중요한가요?",
                "반응형 웹이란 무엇인가요?",
                "localStorage는 언제 사용하나요?",
                "웹 접근성이 중요한 이유는 무엇인가요?"
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                "Virtual DOM의 동작 원리를 설명해 주세요.",
                "상태 관리 라이브러리가 필요한 이유는 무엇인가요?",
                "브라우저 캐시 전략을 설명해 주세요.",
                "CORS 문제가 발생하는 이유는 무엇인가요?",
                "컴포넌트 분리 기준은 무엇인가요?",
                "폼 검증을 프론트에서 하는 이유는 무엇인가요?",
                "메모이제이션은 언제 필요한가요?",
                "이미지 최적화 전략에는 어떤 것들이 있나요?",
                "CSR의 SEO 한계를 어떻게 해결할 수 있나요?",
                "웹 성능 측정 지표에는 무엇이 있나요?"
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                "대규모 SPA에서 상태 폭발을 어떻게 관리하나요?",
                "렌더링 성능 병목을 찾는 방법은 무엇인가요?",
                "SSR 환경에서 인증 처리는 어떻게 설계하나요?",
                "Micro Frontend의 장단점을 설명해 주세요.",
                "브라우저 메모리 누수 원인은 무엇인가요?",
                "웹 보안(XSS, CSRF)을 프론트에서 어떻게 대응하나요?",
                "Code Splitting 전략을 설명해 주세요.",
                "디자인 시스템을 구축하는 이유는 무엇인가요?",
                "프론트엔드 테스트 전략을 설명해 주세요.",
                "사용자 경험을 수치로 개선하는 방법은 무엇인가요?"
        ));

        return Map.copyOf(map);
    }
}
