package com.mockio.common_ai_contractor.generator.question;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewMode;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

import java.util.List;

public record GenerateQuestionCommand(
        Long userId,
        InterviewTrack track,
        InterviewDifficulty difficulty,
        InterviewMode interviewMode,
        Integer answerTimeSeconds,
        int questionCount,
        List<String> primaryTag
) {}
