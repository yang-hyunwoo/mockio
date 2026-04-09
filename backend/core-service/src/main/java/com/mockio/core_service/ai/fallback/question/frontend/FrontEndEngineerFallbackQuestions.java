package com.mockio.core_service.ai.fallback.question.frontend;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.core_service.ai.fallback.FallbackQuestion;
import com.mockio.common_ai_contractor.generator.question.Question;

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
                        new Question(
                                "SPA 개념",
                                "SPA란 무엇인가요?"
                        ),
                        new Question(
                                "SPA의 장단점",
                                "SPA 구조가 사용자 경험과 초기 로딩 성능에 어떤 장단점을 가지는지 설명해 주세요."
                        ),
                        "SPA",
                        Set.of("SPA", "Web", "Architecture")
                ),
                new FallbackQuestion(
                        new Question(
                                "HTML vs CSS",
                                "HTML과 CSS의 역할 차이는 무엇인가요?"
                        ),
                        new Question(
                                "구조와 표현 분리",
                                "HTML과 CSS를 분리하는 것이 유지보수성과 협업 측면에서 왜 중요한가요?"
                        ),
                        "HTML",
                        Set.of("HTML", "CSS", "Frontend")
                ),
                new FallbackQuestion(
                        new Question(
                                "브라우저 렌더링",
                                "브라우저 렌더링 과정은 어떻게 되나요?"
                        ),
                        new Question(
                                "렌더링 최적화 포인트",
                                "Reflow와 Repaint를 줄이기 위해 어떤 방식으로 DOM 조작을 해야 하나요?"
                        ),
                        "Rendering",
                        Set.of("Rendering", "Browser", "DOM")
                ),
                new FallbackQuestion(
                        new Question(
                                "이벤트 버블링",
                                "이벤트 버블링이란 무엇인가요?"
                        ),
                        new Question(
                                "이벤트 전파 제어",
                                "이벤트 버블링과 캡처링을 활용해 이벤트 처리를 최적화하는 방법은 무엇인가요?"
                        ),
                        "Event",
                        Set.of("Event", "DOM", "JavaScript")
                ),
                new FallbackQuestion(
                        new Question(
                                "상태 관리 기초",
                                "상태(state)란 무엇인가요?"
                        ),
                        new Question(
                                "상태와 렌더링 관계",
                                "React에서 상태 변화가 렌더링과 어떤 관계를 가지며 왜 중요한가요?"
                        ),
                        "State",
                        Set.of("State", "Component", "React")
                ),
                new FallbackQuestion(
                        new Question(
                                "CSR vs SSR",
                                "CSR과 SSR의 차이는 무엇인가요?"
                        ),
                        new Question(
                                "CSR/SSR 선택 기준",
                                "SEO, 초기 로딩 속도, 사용자 경험 관점에서 CSR과 SSR을 어떻게 선택해야 하나요?"
                        ),
                        "CSR",
                        Set.of("CSR", "SSR", "Rendering")
                ),
                new FallbackQuestion(
                        new Question(
                                "API 에러 처리",
                                "API 호출 시 에러 처리는 왜 중요한가요?"
                        ),
                        new Question(
                                "에러 처리 UX",
                                "네트워크 에러, 서버 에러, 사용자 입력 에러를 구분해 처리해야 하는 이유는 무엇인가요?"
                        ),
                        "ErrorHandling",
                        Set.of("API", "ErrorHandling", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "반응형 웹",
                                "반응형 웹이란 무엇인가요?"
                        ),
                        new Question(
                                "반응형 설계 기준",
                                "미디어 쿼리와 레이아웃 구조를 통해 반응형 디자인을 구현할 때 핵심 고려사항은 무엇인가요?"
                        ),
                        "Responsive",
                        Set.of("Responsive", "CSS", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "localStorage 사용",
                                "localStorage는 언제 사용하나요?"
                        ),
                        new Question(
                                "스토리지 선택 기준",
                                "localStorage, sessionStorage, cookie 중 어떤 기준으로 선택해야 하나요?"
                        ),
                        "LocalStorage",
                        Set.of("LocalStorage", "Browser", "State")
                ),
                new FallbackQuestion(
                        new Question(
                                "웹 접근성",
                                "웹 접근성이 중요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "접근성 구현 방법",
                                "ARIA 속성, 키보드 접근성, 색 대비 등을 실제 UI에 어떻게 적용할 수 있나요?"
                        ),
                        "Accessibility",
                        Set.of("Accessibility", "UX", "Web")
                )
        ));

        map.put(InterviewDifficulty.MEDIUM, List.of(
                new FallbackQuestion(
                        new Question(
                                "Virtual DOM",
                                "Virtual DOM의 동작 원리를 설명해 주세요."
                        ),
                        new Question(
                                "Virtual DOM 성능",
                                "Virtual DOM이 실제 DOM 조작 대비 성능을 개선하는 이유는 무엇인가요?"
                        ),
                        "VirtualDOM",
                        Set.of("VirtualDOM", "React", "Rendering")
                ),
                new FallbackQuestion(
                        new Question(
                                "상태 관리 라이브러리",
                                "상태 관리 라이브러리가 필요한 이유는 무엇인가요?"
                        ),
                        new Question(
                                "상태 관리 선택 기준",
                                "Redux, Zustand, React Query 등을 상황에 따라 어떻게 선택해야 하나요?"
                        ),
                        "StateManagement",
                        Set.of("StateManagement", "Redux", "Frontend")
                ),
                new FallbackQuestion(
                        new Question(
                                "브라우저 캐시 전략",
                                "브라우저 캐시 전략을 설명해 주세요."
                        ),
                        new Question(
                                "캐시 무효화 문제",
                                "정적 자산 캐싱 시 cache busting 전략이 필요한 이유는 무엇인가요?"
                        ),
                        "Cache",
                        Set.of("Cache", "HTTP", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "CORS 원인",
                                "CORS 문제가 발생하는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "CORS 해결 방식",
                                "CORS 문제를 서버 설정과 프론트 설정 관점에서 각각 어떻게 해결할 수 있나요?"
                        ),
                        "CORS",
                        Set.of("CORS", "Security", "Browser")
                ),
                new FallbackQuestion(
                        new Question(
                                "컴포넌트 분리 기준",
                                "컴포넌트 분리 기준은 무엇인가요?"
                        ),
                        new Question(
                                "재사용성과 책임 분리",
                                "컴포넌트를 재사용 가능하게 만들기 위해 어떤 기준으로 책임을 나눠야 하나요?"
                        ),
                        "Component",
                        Set.of("Component", "Architecture", "React")
                ),
                new FallbackQuestion(
                        new Question(
                                "폼 검증",
                                "폼 검증을 프론트에서 하는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "프론트 vs 백 검증",
                                "폼 검증을 프론트와 백엔드에서 각각 해야 하는 이유를 설명해 주세요."
                        ),
                        "Validation",
                        Set.of("Form", "Validation", "UX")
                ),
                new FallbackQuestion(
                        new Question(
                                "메모이제이션",
                                "메모이제이션은 언제 필요한가요?"
                        ),
                        new Question(
                                "과도한 메모이제이션 문제",
                                "useMemo, useCallback을 과도하게 사용할 경우 발생할 수 있는 문제는 무엇인가요?"
                        ),
                        "Memoization",
                        Set.of("Memoization", "Performance", "React")
                ),
                new FallbackQuestion(
                        new Question(
                                "이미지 최적화",
                                "이미지 최적화 전략에는 어떤 것들이 있나요?"
                        ),
                        new Question(
                                "이미지 로딩 전략",
                                "lazy loading, webp, responsive image 등을 활용한 최적화 전략을 설명해 주세요."
                        ),
                        "ImageOptimization",
                        Set.of("ImageOptimization", "Performance", "Web")
                ),
                new FallbackQuestion(
                        new Question(
                                "CSR SEO 한계",
                                "CSR의 SEO 한계를 어떻게 해결할 수 있나요?"
                        ),
                        new Question(
                                "SEO 개선 전략",
                                "SSR, SSG, 메타 태그 전략을 활용해 SEO를 개선하는 방법을 설명해 주세요."
                        ),
                        "SEO",
                        Set.of("SEO", "SSR", "Rendering")
                ),
                new FallbackQuestion(
                        new Question(
                                "웹 성능 지표",
                                "웹 성능 측정 지표에는 무엇이 있나요?"
                        ),
                        new Question(
                                "Core Web Vitals",
                                "LCP, FID, CLS 같은 Core Web Vitals가 사용자 경험에 어떤 영향을 주는지 설명해 주세요."
                        ),
                        "WebVitals",
                        Set.of("WebVitals", "Performance", "Metrics")
                )
        ));

        map.put(InterviewDifficulty.HARD, List.of(
                new FallbackQuestion(
                        new Question(
                                "상태 폭발 관리",
                                "대규모 SPA에서 상태 폭발을 어떻게 관리하나요?"
                        ),
                        new Question(
                                "상태 구조 설계",
                                "글로벌 상태와 로컬 상태를 어떻게 구분하고 설계해야 유지보수가 쉬워지나요?"
                        ),
                        "StateManagement",
                        Set.of("StateManagement", "Architecture", "SPA")
                ),
                new FallbackQuestion(
                        new Question(
                                "렌더링 병목 분석",
                                "렌더링 성능 병목을 찾는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "성능 분석 도구 활용",
                                "React DevTools Profiler를 활용해 병목을 분석하는 방법을 설명해 주세요."
                        ),
                        "Profiling",
                        Set.of("Performance", "Profiling", "Rendering")
                ),
                new FallbackQuestion(
                        new Question(
                                "SSR 인증 설계",
                                "SSR 환경에서 인증 처리는 어떻게 설계하나요?"
                        ),
                        new Question(
                                "SSR 보안 고려사항",
                                "쿠키 기반 인증과 토큰 기반 인증을 SSR 환경에서 어떻게 다르게 처리해야 하나요?"
                        ),
                        "Authentication",
                        Set.of("SSR", "Authentication", "Security")
                ),
                new FallbackQuestion(
                        new Question(
                                "Micro Frontend",
                                "Micro Frontend의 장단점을 설명해 주세요."
                        ),
                        new Question(
                                "아키텍처 선택 기준",
                                "Micro Frontend를 도입할지 여부를 팀 규모와 서비스 구조 관점에서 어떻게 판단하나요?"
                        ),
                        "MicroFrontend",
                        Set.of("MicroFrontend", "Architecture", "Scalability")
                ),
                new FallbackQuestion(
                        new Question(
                                "메모리 누수",
                                "브라우저 메모리 누수 원인은 무엇인가요?"
                        ),
                        new Question(
                                "누수 방지 방법",
                                "이벤트 리스너, 타이머, 클로저로 인한 메모리 누수를 어떻게 방지할 수 있나요?"
                        ),
                        "MemoryLeak",
                        Set.of("MemoryLeak", "JavaScript", "Performance")
                ),
                new FallbackQuestion(
                        new Question(
                                "웹 보안 대응",
                                "웹 보안(XSS, CSRF)을 프론트에서 어떻게 대응하나요?"
                        ),
                        new Question(
                                "보안 전략 구체화",
                                "XSS 방지를 위한 escaping, CSP 설정과 CSRF 토큰 전략을 설명해 주세요."
                        ),
                        "XSS",
                        Set.of("Security", "XSS", "CSRF")
                ),
                new FallbackQuestion(
                        new Question(
                                "Code Splitting",
                                "Code Splitting 전략을 설명해 주세요."
                        ),
                        new Question(
                                "번들 최적화",
                                "동적 import와 lazy loading을 활용한 번들 최적화 방법을 설명해 주세요."
                        ),
                        "CodeSplitting",
                        Set.of("CodeSplitting", "Performance", "Bundler")
                ),
                new FallbackQuestion(
                        new Question(
                                "디자인 시스템 구축",
                                "디자인 시스템을 구축하는 이유는 무엇인가요?"
                        ),
                        new Question(
                                "디자인 시스템 운영",
                                "디자인 시스템을 지속적으로 유지하고 확장하기 위한 관리 전략은 무엇인가요?"
                        ),
                        "DesignSystem",
                        Set.of("DesignSystem", "Consistency", "UI")
                ),
                new FallbackQuestion(
                        new Question(
                                "프론트 테스트 전략",
                                "프론트엔드 테스트 전략을 설명해 주세요."
                        ),
                        new Question(
                                "테스트 레벨 분리",
                                "단위 테스트, 통합 테스트, E2E 테스트를 어떻게 구분하고 적용해야 하나요?"
                        ),
                        "Testing",
                        Set.of("Testing", "Jest", "Frontend")
                ),
                new FallbackQuestion(
                        new Question(
                                "UX 개선 수치화",
                                "사용자 경험을 수치로 개선하는 방법은 무엇인가요?"
                        ),
                        new Question(
                                "UX 지표 설계",
                                "전환율, 이탈률, 체류 시간 등을 활용해 UX 개선 효과를 어떻게 측정할 수 있나요?"
                        ),
                        "Analytics",
                        Set.of("UX", "Analytics", "Metrics")
                )
        ));

        return Map.copyOf(map);
    }
}