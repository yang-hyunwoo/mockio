package com.mockio.ai_service.fallback.question.frontend;

import com.mockio.ai_service.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrontEndEngineerFallbackQuestions {

    private FrontEndEngineerFallbackQuestions() {}

    public static Map<InterviewDifficulty, List<FallbackQuestion>> byDifficulty() {
        Map<InterviewDifficulty, List<FallbackQuestion>> map = new EnumMap<>(InterviewDifficulty.class);

        map.put(InterviewDifficulty.EASY, List.of(
                new FallbackQuestion(
                        "SPA 개념",
                        "SPA란 무엇인가요?",
                        Set.of("SPA", "Web", "Architecture")
                ),
                new FallbackQuestion(
                        "HTML vs CSS",
                        "HTML과 CSS의 역할 차이는 무엇인가요?",
                        Set.of("HTML", "CSS", "Frontend")
                ),
                new FallbackQuestion(
                        "브라우저 렌더링",
                        "브라우저 렌더링 과정은 어떻게 되나요?",
                        Set.of("Rendering", "Browser", "DOM")
                ),
                new FallbackQuestion(
                        "이벤트 버블링",
                        "이벤트 버블링이란 무엇인가요?",
                        Set.of("Event", "DOM", "JavaScript")
                ),
                new FallbackQuestion(
                        "상태 관리 기초",
                        "상태(state)란 무엇인가요?",
                        Set.of("State", "Component", "React")
                ),
                new FallbackQuestion(
                        "CSR vs SSR",
                        "CSR과 SSR의 차이는 무엇인가요?",
                        Set.of("CSR", "SSR", "Rendering")
                ),
                new FallbackQuestion(
                        "API 에러 처리",
                        "API 호출 시 에러 처리는 왜 중요한가요?",
                        Set.of("API", "ErrorHandling", "UX")
                ),
                new FallbackQuestion(
                        "반응형 웹",
                        "반응형 웹이란 무엇인가요?",
                        Set.of("Responsive", "CSS", "UX")
                ),
                new FallbackQuestion(
                        "localStorage 사용",
                        "localStorage는 언제 사용하나요?",
                        Set.of("LocalStorage", "Browser", "State")
                ),
                new FallbackQuestion(
                        "웹 접근성",
                        "웹 접근성이 중요한 이유는 무엇인가요?",
                        Set.of("Accessibility", "UX", "Web")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        "Virtual DOM",
                        "Virtual DOM의 동작 원리를 설명해 주세요.",
                        Set.of("VirtualDOM", "React", "Rendering")
                ),
                new FallbackQuestion(
                        "상태 관리 라이브러리",
                        "상태 관리 라이브러리가 필요한 이유는 무엇인가요?",
                        Set.of("StateManagement", "Redux", "Frontend")
                ),
                new FallbackQuestion(
                        "브라우저 캐시 전략",
                        "브라우저 캐시 전략을 설명해 주세요.",
                        Set.of("Cache", "HTTP", "Performance")
                ),
                new FallbackQuestion(
                        "CORS 원인",
                        "CORS 문제가 발생하는 이유는 무엇인가요?",
                        Set.of("CORS", "Security", "Browser")
                ),
                new FallbackQuestion(
                        "컴포넌트 분리 기준",
                        "컴포넌트 분리 기준은 무엇인가요?",
                        Set.of("Component", "Architecture", "React")
                ),
                new FallbackQuestion(
                        "폼 검증",
                        "폼 검증을 프론트에서 하는 이유는 무엇인가요?",
                        Set.of("Form", "Validation", "UX")
                ),
                new FallbackQuestion(
                        "메모이제이션",
                        "메모이제이션은 언제 필요한가요?",
                        Set.of("Memoization", "Performance", "React")
                ),
                new FallbackQuestion(
                        "이미지 최적화",
                        "이미지 최적화 전략에는 어떤 것들이 있나요?",
                        Set.of("ImageOptimization", "Performance", "Web")
                ),
                new FallbackQuestion(
                        "CSR SEO 한계",
                        "CSR의 SEO 한계를 어떻게 해결할 수 있나요?",
                        Set.of("SEO", "SSR", "Rendering")
                ),
                new FallbackQuestion(
                        "웹 성능 지표",
                        "웹 성능 측정 지표에는 무엇이 있나요?",
                        Set.of("WebVitals", "Performance", "Metrics")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        "상태 폭발 관리",
                        "대규모 SPA에서 상태 폭발을 어떻게 관리하나요?",
                        Set.of("StateManagement", "Architecture", "SPA")
                ),
                new FallbackQuestion(
                        "렌더링 병목 분석",
                        "렌더링 성능 병목을 찾는 방법은 무엇인가요?",
                        Set.of("Performance", "Profiling", "Rendering")
                ),
                new FallbackQuestion(
                        "SSR 인증 설계",
                        "SSR 환경에서 인증 처리는 어떻게 설계하나요?",
                        Set.of("SSR", "Authentication", "Security")
                ),
                new FallbackQuestion(
                        "Micro Frontend",
                        "Micro Frontend의 장단점을 설명해 주세요.",
                        Set.of("MicroFrontend", "Architecture", "Scalability")
                ),
                new FallbackQuestion(
                        "메모리 누수",
                        "브라우저 메모리 누수 원인은 무엇인가요?",
                        Set.of("MemoryLeak", "JavaScript", "Performance")
                ),
                new FallbackQuestion(
                        "웹 보안 대응",
                        "웹 보안(XSS, CSRF)을 프론트에서 어떻게 대응하나요?",
                        Set.of("Security", "XSS", "CSRF")
                ),
                new FallbackQuestion(
                        "Code Splitting",
                        "Code Splitting 전략을 설명해 주세요.",
                        Set.of("CodeSplitting", "Performance", "Bundler")
                ),
                new FallbackQuestion(
                        "디자인 시스템 구축",
                        "디자인 시스템을 구축하는 이유는 무엇인가요?",
                        Set.of("DesignSystem", "Consistency", "UI")
                ),
                new FallbackQuestion(
                        "프론트 테스트 전략",
                        "프론트엔드 테스트 전략을 설명해 주세요.",
                        Set.of("Testing", "Jest", "Frontend")
                ),
                new FallbackQuestion(
                        "UX 개선 수치화",
                        "사용자 경험을 수치로 개선하는 방법은 무엇인가요?",
                        Set.of("UX", "Analytics", "Metrics")
                )
        ));

        return Map.copyOf(map);
    }

}
