package com.mockio.user_service.repository;

/**
 * UserRepository
 *
 * 회원 엔티티에 대한 CRUD 및 사용자 정의 조회 기능을 제공하는 Repository.
 * Spring Data JPA를 기반으로 구현됩니다.
 */


import com.mockio.user_service.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {


    /**
     * keycloakId로 유저 프로필 조회 합니다.
     *
     * @param keycloakId
     * @return 활성 상태의 유저 프로필 조회
     */
    Optional<UserProfile> findByKeycloakId(String keycloakId);

    boolean existsByNickname(String nickname);
}
