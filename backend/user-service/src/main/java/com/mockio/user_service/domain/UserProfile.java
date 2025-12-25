package com.mockio.user_service.domain;

/**
 * 유저 프로필 엔티티
 * UserProfile
 */

import com.mockio.common_jpa.domain.BaseTimeEntity;
import com.mockio.user_service.constant.ProfileVisibility;
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
@Table(name = "user_profiles")
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_id", nullable = false, length = 128)
    private String keycloakId;

    @Column(name = "profile_image_id")
    private Long profileImageId;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "bio", length = 300)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 20)
    private ProfileVisibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    @Builder
    private UserProfile(Long id,
                        String keycloakId,
                        Long profileImageId,
                        String email,
                        String name,
                        String nickname,
                        String bio,
                        ProfileVisibility visibility,
                        UserStatus status,
                        OffsetDateTime lastLoginAt
    ) {

        this.id = id;
        this.keycloakId = keycloakId;
        this.profileImageId = profileImageId;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.bio = bio;
        this.visibility = visibility != null ? visibility : ProfileVisibility.PUBLIC;
        this.status = status != null ? status : UserStatus.ACTIVE;
        this.lastLoginAt = lastLoginAt;
    }

    /**
     * 유저 프로필 생성 메서드
     * @param keycloakId
     * @param email
     * @param name
     * @param nickname
     * @return
     */
    public static UserProfile createUserProfile( String keycloakId,
                                                String email,
                                                String name,
                                                String nickname
    ) {
        return UserProfile.builder()
                .keycloakId(keycloakId)
                .email(email)
                .name(name)
                .nickname(nickname)
                .visibility(ProfileVisibility.PUBLIC)
                .status(UserStatus.ACTIVE)
                .build();
    }


    /** 로그인 시점 갱신 */
    public void updateLastLoginAt() {
        this.lastLoginAt = OffsetDateTime.now();
    }

    /** 프로필 수정 */
    public void updateProfile(String nickname,
                              Long profileImageId ,
                              String bio,
                              ProfileVisibility visibility) {
        this.nickname = nickname;
        this.profileImageId = profileImageId;
        this.bio = bio;
        this.visibility = visibility;
    }

    /** 상태 변경 (정지/탈퇴 등) */
    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id) && Objects.equals(profileImageId, that.profileImageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, profileImageId);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", keycloakId='" + keycloakId + '\'' +
                ", profileImageId=" + profileImageId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", bio='" + bio + '\'' +
                ", visibility=" + visibility +
                ", status=" + status +
                ", lastLoginAt=" + lastLoginAt +
                '}';
    }
}

