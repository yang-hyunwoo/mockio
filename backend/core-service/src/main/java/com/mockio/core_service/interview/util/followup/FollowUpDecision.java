package com.mockio.core_service.interview.util.followup;

public record FollowUpDecision(FollowUpDecisionType type, String reason) {

    public static FollowUpDecision askNormal(String reason) {
        return new FollowUpDecision(FollowUpDecisionType.ASK, reason);
    }

    public static FollowUpDecision skip(String reason) {
        return new FollowUpDecision(FollowUpDecisionType.SKIP, reason);
    }

    public static FollowUpDecision deferToAi(String reason) {
        return new FollowUpDecision(FollowUpDecisionType.DEFER_TO_AI, reason);
    }

    public boolean shouldAskFollowUp() {
        return type == FollowUpDecisionType.ASK;
    }

    public boolean shouldSkip() {
        return type == FollowUpDecisionType.SKIP;
    }

    public boolean shouldDeferToAi() {
        return type == FollowUpDecisionType.DEFER_TO_AI;
    }

    public enum FollowUpDecisionType {
        ASK,
        SKIP,
        DEFER_TO_AI
    }
}