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

    // ===== 로그인 / 계정 정보 =====
    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "fail_login_count", nullable = false)
    private int failLoginCount;

    // ===== 권한 =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserRole role;

    // ===== 상태 =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    // ===== 로그인 제공자 =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProviderEnum provider;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    @Builder
    protected User(Long id,
                   String email,
                   String password,
                   int failLoginCount,
                   UserRole role,
                   UserStatus status,
                   AuthProviderEnum provider,
                   OffsetDateTime lastLoginAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.failLoginCount = failLoginCount;
        this.role = role;
        this.status = status;
        this.provider = provider;
        this.lastLoginAt = lastLoginAt;
    }

    public static User createUser(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .failLoginCount(0)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .provider(AuthProviderEnum.NORMAL)
                .build();
    }

    public void assignProfile(UserProfile profile) {
        this.profile = profile;
        if (profile != null && profile.getUser() != this) {
            profile.assignUser(this);
        }
    }

    public void updateLastLogin() {
        this.lastLoginAt = OffsetDateTime.now();
        this.failLoginCount = 0;
    }

    public void increaseFailLoginCount() {
        this.failLoginCount++;
    }

    public void resetFailLoginCount() {
        this.failLoginCount = 0;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }

    public void deactivate() {
        this.status = UserStatus.DELETED;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
