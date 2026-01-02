package com.mockio.interview_service.repository;

import com.mockio.interview_service.domain.UserInterviewSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserInterviewPreferenceRepository
 *
 * 유저 면접 설정 대한 CRUD 기능을 제공하는 Repository.
 * Spring Data JPA를 기반으로 구현됩니다.
 */

public interface UserInterviewSettingRepository extends JpaRepository<UserInterviewSetting, Long> {

    Optional<UserInterviewSetting> findByUserId(String userId);
}
