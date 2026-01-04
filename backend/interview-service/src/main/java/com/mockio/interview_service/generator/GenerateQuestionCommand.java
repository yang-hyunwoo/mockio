package com.mockio.interview_service.generator;

import com.mockio.interview_service.constant.InterviewDifficulty;
import com.mockio.interview_service.constant.InterviewMode;
import com.mockio.interview_service.constant.InterviewTrack;

public record GenerateQuestionCommand(
        String userId,
        InterviewTrack track,
        InterviewDifficulty difficulty,
        InterviewMode interviewMode,
        Integer answerTimeSeconds,
        int questionCount
) {
}
