package com.mockio.interview_service.repository;

import com.mockio.interview_service.domain.UserInterviewSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * UserInterviewPreferenceRepository
 *
 * 유저 면접 설정 대한 CRUD 기능을 제공하는 Repository.
 * Spring Data JPA를 기반으로 구현됩니다.
 */

public interface UserInterviewSettingRepository extends JpaRepository<UserInterviewSetting, Long> {

    Optional<UserInterviewSetting> findByUserId(Long userId);

    @Modifying
    @Query(value = """
        INSERT INTO user_interview_settings
        (user_id, track, difficulty, interview_feedback_style, interview_mode,
         answer_time_seconds, interview_question_count)
        VALUES
        (:userId, :track, :difficulty, :feedbackStyle, :interviewMode,
         :answerTimeSeconds, :interviewQuestionCount)
        ON CONFLICT (user_id) DO NOTHING
        """, nativeQuery = true)
    int insertIfAbsent(
            @Param("userId") Long userId,
            @Param("track") String track,
            @Param("difficulty") String difficulty,
            @Param("feedbackStyle") String feedbackStyle,
            @Param("interviewMode") String interviewMode,
            @Param("answerTimeSeconds") int answerTimeSeconds,
            @Param("interviewQuestionCount") int interviewQuestionCount
    );
}
