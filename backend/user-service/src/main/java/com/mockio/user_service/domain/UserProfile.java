package com.mockio.user_service.domain;

import com.mockio.common_jpa.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long profileImageId;

    @Column(nullable = false, length = 100)
    private String keycloakId;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 20)
    private String phoneNumber;



}
