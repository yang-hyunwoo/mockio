package com.mockio.common_ai_contractor.generator;


import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

public record GenerateQuestionCommand(
        String userId,
        InterviewTrack track,
        InterviewDifficulty difficulty,
        InterviewMode interviewMode,
        Integer answerTimeSeconds,
        int questionCount
) {
}
