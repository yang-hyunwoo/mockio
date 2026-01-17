package com.mockio.interview_service.util.followup;

public record FollowUpDecision(boolean askFollowUp, String reason) {

    public static FollowUpDecision ask(String reason) {
        return new FollowUpDecision(true, reason);
    }

    public static FollowUpDecision skip(String reason) {
        return new FollowUpDecision(false, reason);
    }
}
