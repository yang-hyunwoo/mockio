package com.mockio.core_service.interview.domain;

/**
 * 유저 면접 설정
 * UserProfile
 */

import com.mockio.common_ai_contractor.constant.*;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_000;
import static java.util.Optional.ofNullable;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_interview_settings")
public class UserInterviewSetting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewTrack track;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InterviewDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_feedback_style", nullable = false, length = 30)
    private InterviewFeedbackStyle feedbackStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_mode", nullable = false, length = 30)
    private InterviewMode interviewMode;

    @Column(name = "answer_time_seconds", nullable = false)
    private Integer answerTimeSeconds;

    @Column(name = "interview_question_count", nullable = false)
    private int interviewQuestionCount;

    @OneToMany(
            mappedBy = "interviewSetting",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<InterviewKeyword> keywords = new ArrayList<>();



    @Builder
    private UserInterviewSetting(
        Long id,
        Long userId,
        InterviewTrack track,
        InterviewDifficulty difficulty,
        InterviewFeedbackStyle feedbackStyle,
        InterviewMode interviewMode,
        Integer answerTimeSeconds,
        int interviewQuestionCount
    ) {
        this.id = id;
        this.userId = userId;
        this.track = track;
        this.difficulty = difficulty;
        this.feedbackStyle = feedbackStyle;
        this.interviewMode = interviewMode;
        this.answerTimeSeconds = answerTimeSeconds;
        this.interviewQuestionCount = interviewQuestionCount;
    }

    public static UserInterviewSetting createUserInterviewPreference(Long userId) {
        return UserInterviewSetting.builder()
                .userId(userId)
                .track(InterviewTrack.GENERAL)
                .difficulty(InterviewDifficulty.MEDIUM)
                .feedbackStyle(InterviewFeedbackStyle.FRIENDLY)
                .interviewMode(InterviewMode.TEXT)
                .answerTimeSeconds(120)
                .interviewQuestionCount(3)
                .build();
    }

    /**
     * 변경
     *
     * @param track
     * @param difficulty
     * @param feedbackStyle
     * @param interviewMode
     * @param answerTimeSeconds
     */
    public void applyPatch(InterviewTrack track,
                           InterviewDifficulty difficulty,
                           InterviewFeedbackStyle feedbackStyle,
                           InterviewMode interviewMode,
                           Integer answerTimeSeconds,
                           int interviewQuestionCount,
                           List<String> interviewKeyword) {
        ofNullable(track).ifPresent(this::changeTrack);
        ofNullable(difficulty).ifPresent(this::changeDifficulty);
        ofNullable(feedbackStyle).ifPresent(this::changeFeedbackStyle);
        ofNullable(interviewMode).ifPresent(this::changeInterviewMode);
        ofNullable(answerTimeSeconds).ifPresent(this::changeAnswerTimeSeconds);
        changeInterviewQuestionCount(interviewQuestionCount);
        replaceKeywords(interviewKeyword);
    }

    /**
     *  면접 분야 변경
     * @param track
     */
    public void changeTrack(InterviewTrack track) {
        if (this.track != track) {
            this.clearKeywords();
        }
        this.track = track;

    }

    /**
     * 면접 난이도 변경
     * @param interviewDifficulty
     */
    public void changeDifficulty(InterviewDifficulty interviewDifficulty) {
        this.difficulty = interviewDifficulty;
    }

    /**
     * 면접 피드백 변경
     * @param feedbackStyle
     */
    public void changeFeedbackStyle(InterviewFeedbackStyle feedbackStyle) {
        this.feedbackStyle = feedbackStyle;
    }

    /**
     * 면접 모드 변경
     * @param interviewMode
     */
    public void changeInterviewMode(InterviewMode interviewMode) {
        this.interviewMode = interviewMode;
    }

    /**
     * 면접 답변 시간 변경
     * @param answerTimeSeconds
     */
    public void changeAnswerTimeSeconds(int answerTimeSeconds) {
        this.answerTimeSeconds = answerTimeSeconds;
    }

    /**
     * 면접 질문 갯수 변경
     * @param interviewQuestionCount
     */
    public void changeInterviewQuestionCount(int interviewQuestionCount) {
        this.interviewQuestionCount = interviewQuestionCount;
    }

    public void replaceKeywords(List<String> keywordValues) {
        validateKeyword(keywordValues);
        this.keywords.clear();

        if (keywordValues == null || keywordValues.isEmpty()) {
            return;
        }

        keywordValues.stream()
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .forEach(this::addKeyword);
    }

    private void validateKeyword(List<String> keyword) {
        for (String s : keyword) {
            if (s.length() > 50) {
                throw new CustomApiException(
                        ERR_000.getHttpStatus(),
                        ERR_000,
                        "키워드는 최대 50자까지 입력 가능합니다."
                );
            }
        }
    }

    public void clearKeywords() {
        this.keywords.clear();
    }

    private void addKeyword(String value) {
        InterviewKeyword keyword = InterviewKeyword.create(this, value);
        this.keywords.add(keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserInterviewSetting that = (UserInterviewSetting) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserInterviewSetting{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", track=" + track +
                ", difficulty=" + difficulty +
                ", feedbackStyle=" + feedbackStyle +
                ", interviewMode=" + interviewMode +
                ", answerTimeSeconds=" + answerTimeSeconds +
                '}';
    }

}
