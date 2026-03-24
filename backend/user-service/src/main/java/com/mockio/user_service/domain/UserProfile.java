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

import static java.util.Optional.*;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_profiles")
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_profiles_user"))
    private User user;

    @Column(name = "profile_image_id")
    private Long profileImageId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "nickname", nullable = false, length = 30)
    private String nickname;

    @Column(name = "bio", length = 300)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 20)
    private ProfileVisibility visibility;

    @Builder
    private UserProfile(Long id,
                        User user,
                        Long profileImageId,
                        String name,
                        String nickname,
                        String bio,
                        ProfileVisibility visibility) {
        this.id = id;
        this.user = user;
        this.profileImageId = profileImageId;
        this.name = name;
        this.nickname = nickname;
        this.bio = bio;
        this.visibility = visibility != null ? visibility : ProfileVisibility.PUBLIC;
    }

    public static UserProfile createUserProfile(User user,
                                                String name,
                                                String nickname) {
        UserProfile profile = UserProfile.builder()
                .user(user)
                .name(name)
                .nickname(nickname)
                .visibility(ProfileVisibility.PUBLIC)
                .build();

        if (user != null) {
            user.assignProfile(profile);
        }

        return profile;
    }

    public void assignUser(User user) {
        this.user = user;
        if (user != null && user.getProfile() != this) {
            user.assignProfile(this);
        }
    }

    public void applyPatch(String nickname,
                           Long profileImageId) {
        ofNullable(nickname).filter(s -> !s.isBlank()).ifPresent(this::changeNickname);
        ofNullable(profileImageId).ifPresent(this::changeProfileImage);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeProfileImage(Long profileImageId) {
        this.profileImageId = profileImageId;
    }

    public void changeBio(String bio) {
        this.bio = bio;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeVisibility(ProfileVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}

