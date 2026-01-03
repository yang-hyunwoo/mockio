package com.mockio.interview_service.repository;

import com.mockio.interview_service.PostgresDataJpaTest;
import com.mockio.interview_service.constant.FeedbackStyle;
import com.mockio.interview_service.constant.InterviewDifficulty;
import com.mockio.interview_service.constant.InterviewMode;
import com.mockio.interview_service.constant.InterviewTrack;
import com.mockio.interview_service.domain.UserInterviewSetting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserInterviewSettingRepositoryTest extends PostgresDataJpaTest {

    @Autowired
    private UserInterviewSettingRepository repository;

    @Test
    @DisplayName("userId로 UserInterviewSetting을 조회한다")
    void findByUserId_success() {
        // given
        String userId = "test-user-123";

        UserInterviewSetting setting = UserInterviewSetting.builder()
                .userId(userId)
                .track(InterviewTrack.DATA)
                .difficulty(InterviewDifficulty.EASY)
                .feedbackStyle(FeedbackStyle.COACHING)
                .interviewMode(InterviewMode.TEXT)
                .answerTimeSeconds(120)
                .build();

        repository.save(setting);

        // when
        Optional<UserInterviewSetting> result =
                repository.findByUserId(userId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("존재하지 않는 userId 조회 시 Optional.empty 반환")
    void findByUserId_empty() {
        // given
        String userId = "not-exist-user";

        // when
        Optional<UserInterviewSetting> result =
                repository.findByUserId(userId);

        // then
        assertThat(result).isEmpty();
    }
}