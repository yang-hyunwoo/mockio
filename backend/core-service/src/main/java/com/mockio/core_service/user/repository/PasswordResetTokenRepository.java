package com.mockio.core_service.user.repository;

import com.mockio.core_service.user.domain.PasswordResetToken;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken , Long>{

    @Modifying
    @Query("""
                UPDATE PasswordResetToken t
                SET t.used = true,
                    t.usedAt = CURRENT_TIMESTAMP
                WHERE t.userId = :userId
                  AND t.used = false
            """)
    void invalidateAll(Long userId);

    @Query("""
                SELECT t
                FROM PasswordResetToken t
                WHERE t.token = :token
                  AND t.used = false
                  AND t.expiredAt > CURRENT_TIMESTAMP
            """)
    Optional<PasswordResetToken> findValidToken(@Param("token") String token);

}
