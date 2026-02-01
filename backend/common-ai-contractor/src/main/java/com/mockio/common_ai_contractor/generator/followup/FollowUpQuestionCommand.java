package com.mockio.common_ai_contractor.generator.followup;

import com.mockio.common_ai_contractor.constant.InterviewDifficulty;
import com.mockio.common_ai_contractor.constant.InterviewFeedbackStyle;
import com.mockio.common_ai_contractor.constant.InterviewTrack;


public record FollowUpQuestionCommand(
        InterviewTrack interviewTrack,
        InterviewDifficulty interviewDifficulty,
        InterviewFeedbackStyle feedbackStyle,      // 친절/압박
        String followUpReason,              //꼬리질문 이유
        QAPair recentQa
) {
    public record QAPair(String question, String answer) {}
}
