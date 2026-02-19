package com.mockio.common_ai_contractor.generator.deepdive;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewTrack;

public record DeepDiveCommand (
        InterviewTrack interviewTrack,
        InterviewDifficulty interviewDifficulty,
        String question,
        String answer
) {}
