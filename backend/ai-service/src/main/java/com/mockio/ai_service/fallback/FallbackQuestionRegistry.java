package com.mockio.ai_service.fallback;

import com.mockio.ai_service.fallback.question.backend.BackEndEngineerFallbackQuestions;
import com.mockio.ai_service.fallback.question.business.BusinessFallbackQuestions;
import com.mockio.ai_service.fallback.question.data.DataEngineerFallbackQuestions;
import com.mockio.ai_service.fallback.question.design.DesignFallbackQuestions;
import com.mockio.ai_service.fallback.question.frontend.FrontEndEngineerFallbackQuestions;
import com.mockio.ai_service.fallback.question.general.GeneralFallbackQuestions;
import com.mockio.ai_service.fallback.question.hr.HrFallbackQuestions;
import com.mockio.ai_service.fallback.question.marketing.MarketingFallbackQuestions;
import com.mockio.ai_service.fallback.question.product.ProductFallbackQuestions;
import com.mockio.ai_service.fallback.question.sales.SalesFallbackQuestions;
import com.mockio.ai_service.fallback.question.server.ServerEngineerFallbackQuestions;
import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class FallbackQuestionRegistry {

    private static final Map<InterviewTrack, Map<InterviewDifficulty, List<String>>> STORE;

    static {
        Map<InterviewTrack, Map<InterviewDifficulty, List<String>>> map =
                new EnumMap<>(InterviewTrack.class);

        map.put(InterviewTrack.DATA_ENGINEER, DataEngineerFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.BACKEND_ENGINEER, BackEndEngineerFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.FRONTEND_ENGINEER, FrontEndEngineerFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.SERVER_ENGINEER, ServerEngineerFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.DESIGN, DesignFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.PRODUCT, ProductFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.BUSINESS, BusinessFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.MARKETING, MarketingFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.SALES, SalesFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.HR, HrFallbackQuestions.byDifficulty());
        map.put(InterviewTrack.GENERAL, GeneralFallbackQuestions.byDifficulty());

        STORE = Map.copyOf(map);
    }

    private FallbackQuestionRegistry() {}

    /**
     * downgrade 규칙:
     * 요청 난이도가 없으면 MEDIUM, 그것도 없으면 EASY, 그래도 없으면 GENERAL.MEDIUM
     */
    public static List<String> get(InterviewTrack track, InterviewDifficulty difficulty) {
        InterviewDifficulty d = (difficulty == null) ? InterviewDifficulty.MEDIUM : difficulty;

        Map<InterviewDifficulty, List<String>> byDifficulty =
                STORE.getOrDefault(track, STORE.getOrDefault(InterviewTrack.GENERAL, Map.of()));

        List<String> picked = byDifficulty.get(d);
        if (picked != null && !picked.isEmpty()) return picked;

        // downgrade: HARD -> MEDIUM -> EASY
        if (d == InterviewDifficulty.HARD) {
            picked = byDifficulty.get(InterviewDifficulty.MEDIUM);
            if (picked != null && !picked.isEmpty()) return picked;
        }
        picked = byDifficulty.get(InterviewDifficulty.EASY);
        if (picked != null && !picked.isEmpty()) return picked;

        // 최후의 안전장치
        Map<InterviewDifficulty, List<String>> general = STORE.getOrDefault(InterviewTrack.GENERAL, Map.of());

        return general.getOrDefault(InterviewDifficulty.MEDIUM, List.of());
    }

}
