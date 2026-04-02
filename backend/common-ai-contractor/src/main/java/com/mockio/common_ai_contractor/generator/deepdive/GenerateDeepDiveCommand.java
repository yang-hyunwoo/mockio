package com.mockio.common_ai_contractor.generator.deepdive;

/**
 * 딥 다이브 ai 요청 DTO
 */

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

public record GenerateDeepDiveCommand(
        InterviewTrack interviewTrack,
        InterviewDifficulty interviewDifficulty,
        String basicQuestion,
        String basicAnswer,
        String question,
        String answer
) {}

