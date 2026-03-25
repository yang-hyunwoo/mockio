package com.mockio.core_service.user.repository;

import com.mockio.core_service.user.constant.AuthProviderEnum;
import com.mockio.core_service.user.constant.UserStatus;
import com.mockio.core_service.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndProvider(String email,
                                            AuthProviderEnum providerType);

    Optional<User> findByIdAndStatus(Long userId, UserStatus status);

    Optional<User> findByEmailAndProviderAndStatus(String email,
                                          AuthProviderEnum providerType,
                                                   UserStatus userStatus);

    Optional<User> findByIdAndStatusAndProvider(Long id,
                                                UserStatus userStatus,
                                                AuthProviderEnum providerEnum
    );

}
