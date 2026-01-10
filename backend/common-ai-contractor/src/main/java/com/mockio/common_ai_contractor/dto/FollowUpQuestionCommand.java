package com.mockio.common_ai_contractor.dto;

import java.util.List;

public record FollowUpQuestionCommand(
        String track,
        String difficulty,
        String feedbackStyle,      // 친절/압박
        List<QAPair> recentQa      // 최신이 마지막, 최대 3개 권장
) {
    public record QAPair(String question, String answer) {}
}
