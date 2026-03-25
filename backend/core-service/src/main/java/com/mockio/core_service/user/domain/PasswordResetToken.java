package com.mockio.core_service.user.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PasswordResetToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @Column(nullable = false)
    private OffsetDateTime expiredAt;

    @Column(nullable = false)
    private boolean used;

    private OffsetDateTime usedAt;


    @Builder
    protected PasswordResetToken(
            Long id,
            Long userId,
            String token,
            OffsetDateTime expiredAt,
            boolean used,
            OffsetDateTime usedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expiredAt = expiredAt;
        this.used = used;
        this.usedAt = usedAt;
    }

    public static PasswordResetToken createPwdResetToken(
            Long userId,
            String token
    ) {
        return PasswordResetToken.builder()
                .userId(userId)
                .token(token)
                .expiredAt(OffsetDateTime.now().plusMinutes(30))
                .used(false)
                .build();
    }

    public void updateResetToken() {
        this.used = true;
        this.expiredAt = OffsetDateTime.now().minusMinutes(5);
        this.usedAt = OffsetDateTime.now();
    }

}
