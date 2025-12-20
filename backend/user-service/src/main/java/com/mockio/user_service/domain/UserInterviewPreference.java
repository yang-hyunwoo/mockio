package com.mockio.user_service.domain;

/**
 * 유저 면접 설정
 * UserProfile
 */

import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.user_service.constant.FeedbackStyle;
import com.mockio.user_service.constant.InterviewDifficulty;
import com.mockio.user_service.constant.InterviewMode;
import com.mockio.user_service.constant.InterviewTrack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserInterviewPreference extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewTrack track;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_style", nullable = false, length = 30)
    private FeedbackStyle feedbackStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_mode", nullable = false, length = 30)
    private InterviewMode interviewMode;

    @Column(name = "answer_time_seconds", nullable = false)
    private int answerTimeSeconds;


    @Builder
    private UserInterviewPreference(
        Long id,
        UserProfile userProfile,
        InterviewTrack track,
        InterviewDifficulty difficulty,
        FeedbackStyle feedbackStyle,
        InterviewMode interviewMode,
        int answerTimeSeconds
    ) {
        this.id = id;
        this.userProfile = userProfile;
        this.track = track;
        this.difficulty = difficulty;
        this.feedbackStyle = feedbackStyle;
        this.interviewMode = interviewMode;
        this.answerTimeSeconds = answerTimeSeconds;
    }

    public static UserInterviewPreference createUserInterviewPreference(UserProfile userProfile) {
        return UserInterviewPreference.builder()
                .userProfile(userProfile)
                .track(InterviewTrack.GENERAL)
                .difficulty(InterviewDifficulty.MEDIUM)
                .feedbackStyle(FeedbackStyle.COACHING)
                .interviewMode(InterviewMode.TEXT)
                .answerTimeSeconds(90)
                .build();
    }

    /**
     * 변경
     * @param track
     * @param difficulty
     * @param feedbackStyle
     * @param interviewMode
     * @param answerTimeSeconds
     */
    public void update(InterviewTrack track,
                       InterviewDifficulty difficulty,
                       FeedbackStyle feedbackStyle,
                       InterviewMode interviewMode,
                       int answerTimeSeconds) {
        this.track = track;
        this.difficulty = difficulty;
        this.feedbackStyle = feedbackStyle;
        this.interviewMode = interviewMode;
        this.answerTimeSeconds = answerTimeSeconds;
    }
}
