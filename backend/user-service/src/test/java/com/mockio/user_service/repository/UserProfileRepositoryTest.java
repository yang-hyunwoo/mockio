package com.mockio.user_service.repository;

import com.mockio.user_service.PostgresDataJpaTest;
import com.mockio.user_service.domain.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileRepositoryTest extends PostgresDataJpaTest {


    @Autowired
    UserProfileRepository userProfileRepository;

    @Test
    @DisplayName("UserProfile을 저장하고 keycloakId로 조회할 수 있다")
    void shouldSaveAndFindByKeycloakId() {
        UserProfile user = UserProfile.createUserProfile(
                "kc-123",
                "a@b.com",
                "홍길동",
                "user_test"
        );

        userProfileRepository.save(user);

        Optional<UserProfile> result = userProfileRepository.findByKeycloakId("kc-123");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("a@b.com");
    }
}
