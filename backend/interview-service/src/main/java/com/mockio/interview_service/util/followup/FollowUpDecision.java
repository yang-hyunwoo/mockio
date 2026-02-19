package com.mockio.interview_service.util.followup;

public record FollowUpDecision(boolean askFollowUp, boolean deepDive, String reason) {

    public static FollowUpDecision askNormal(String reason) {
        return new FollowUpDecision(true, false, reason);
    }

    public static FollowUpDecision askDeepDive(String reason) {
        return new FollowUpDecision(true, true, reason);
    }

    public static FollowUpDecision skip(String reason) {
        return new FollowUpDecision(false, false, reason);
    }

}