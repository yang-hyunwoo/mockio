package com.mockio.user_service.domain;

import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.user_service.constant.AuthProviderEnum;
import com.mockio.user_service.constant.UserRole;
import com.mockio.user_service.constant.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== 로그인 정보 =====
    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 255)
    private String password;

    // ===== 권한 =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;

    // ===== 상태 =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    // ===== 소셜 로그인용 =====
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AuthProviderEnum provider;

    private OffsetDateTime lastLoginAt;

    @Builder
    protected User(Long id,
                   String email,
                   String nickname,
                   String password,
                   UserRole role,
                   UserStatus status,
                   AuthProviderEnum provider,
                   OffsetDateTime lastLoginAt
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.status = status;
        this.provider = provider;
        this.lastLoginAt = lastLoginAt;
    }

    public static User createUser(String email,
                                  String password,
                                  String nickname

    ) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .provider(AuthProviderEnum.NORMAL)
                .build();
    }


    public void updateLastLogin() {
        this.lastLoginAt = OffsetDateTime.now();
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void deactivate() {
        this.status = UserStatus.DELETED;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
