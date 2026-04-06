package com.mockio.core_service.user.repository;

import com.mockio.core_service.user.constant.AuthProviderEnum;
import com.mockio.core_service.user.constant.UserStatus;
import com.mockio.core_service.user.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

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
                                                AuthProviderEnum providerEnum);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :userId")
    Optional<User> findByIdForUpdate(@Param("userId") Long userId);
}
