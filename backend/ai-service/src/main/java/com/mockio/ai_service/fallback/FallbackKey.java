package com.mockio.ai_service.fallback;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

public record FallbackKey(InterviewTrack track, InterviewDifficulty difficulty) {

    public static FallbackKey of(InterviewTrack track, InterviewDifficulty difficulty) {
        return new FallbackKey(
                track != null ? track : InterviewTrack.GENERAL,
                difficulty != null ? difficulty : InterviewDifficulty.EASY
        );
    }

}