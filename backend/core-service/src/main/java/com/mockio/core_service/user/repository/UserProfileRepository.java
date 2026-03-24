package com.mockio.core_service.user.repository;

/**
 * UserRepository
 *
 * 유저 엔티티에 대한 CRUD 및 사용자 정의 조회 기능을 제공하는 Repository.
 * Spring Data JPA를 기반으로 구현됩니다.
 */


import com.mockio.core_service.user.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByNickname(String nickname);

}
