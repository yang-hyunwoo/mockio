package com.mockio.user_service.repository;

import com.mockio.user_service.constant.AuthProviderEnum;
import com.mockio.user_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndProvider(String email,
                                            AuthProviderEnum providerType);

    boolean existsByNickname(String nickname);
}
