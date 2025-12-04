package com.mockio.user_service.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import com.mockio.user_service.dto.UserProfileDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.management.Notification;
import java.util.Objects;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String keycloakId;

    private Long profileImageId;

    @Column(length = 10)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 20)
    private String phoneNumber;

    @Builder
    protected UserProfile(Long id,
                           String keycloakId,
                           Long profileImageId,
                           String name,
                           String email,
                           String nickname,
                           String phoneNumber) {
        this.id = id;
        this.keycloakId = keycloakId;
        this.profileImageId = profileImageId;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public static UserProfile fromKeycloakClaims(UserProfileDto userProfileDto) {
        return UserProfile.builder()
                .keycloakId(userProfileDto.keycloakId())
                .name(userProfileDto.name())
                .email(userProfileDto.email())
                .nickname(userProfileDto.nickname())
                .phoneNumber(userProfileDto.phoneNumber())
                .build();
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
                ", profileImageId=" + profileImageId +
                ", keycloakId='" + keycloakId + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
