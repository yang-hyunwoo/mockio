package com.mockio.user_service.service;

import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.response.UserProfileResponse;
import com.mockio.user_service.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        jwt = Jwt.withTokenValue("t")
                .header("alg", "none")
                .subject("kc-123")
                .claim("email", "a@b.com")
                .claim("preferred_username", "user1")
                .claim("name", "홍길동")
                .claim("phone_number", "010-1234-5678")
                .build();
    }

    @Test
    @DisplayName("기존 유저면 lastLogin만 업데이트하고 DTO를 반환한다")
    void shouldUpdateLastLoginAndReturnDtoWhenUserAlreadyExists() {
        // given: 실제 엔티티(또는 충분히 값이 채워진 객체)를 사용해야 Mapper에서 NPE가 안 남
        UserProfile existing = UserProfile.createUserProfile(
                "kc-123",
                "a@b.com",
                "홍길동",
                "user_existing"
        );

        given(userRepository.findByKeycloakId("kc-123"))
                .willReturn(Optional.of(existing));

        // when
        UserProfileResponse result = userProfileService.loadOrCreateFromToken(jwt);

        // then
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("a@b.com");
        assertThat(result.name()).isEqualTo("홍길동");
        assertThat(result.nickname()).isEqualTo("user_existing");

        then(userRepository).should(never()).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("신규 유저면 생성 후 저장하고 DTO를 반환한다")
    void shouldCreateAndSaveNewUserAndReturnDtoWhenUserDoesNotExist() {
        // given
        given(userRepository.findByKeycloakId("kc-123"))
                .willReturn(Optional.empty());
        given(userRepository.existsByNickname(anyString()))
                .willReturn(false);

        // save 시점에 리턴되는 엔티티가 DTO로 변환되므로, 반환 엔티티에도 값이 있어야 함
        UserProfile saved = UserProfile.createUserProfile(
                "kc-123",
                "a@b.com",
                "홍길동",
                "user_new"
        );

        given(userRepository.save(any(UserProfile.class)))
                .willReturn(saved);

        // when
        UserProfileResponse result = userProfileService.loadOrCreateFromToken(jwt);

        // then
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("a@b.com");
        assertThat(result.name()).isEqualTo("홍길동");
        assertThat(result.nickname()).isEqualTo("user_new");

        then(userRepository).should().save(any(UserProfile.class));
    }

    @Test
    @DisplayName("닉네임이 중복이면 중복이 아닐 때까지 재시도한 뒤 저장한다")
    void shouldRetryNicknameGenerationUntilUniqueThenSave() {
        // given
        given(userRepository.findByKeycloakId("kc-123"))
                .willReturn(Optional.empty());

        given(userRepository.existsByNickname(anyString()))
                .willReturn(true, true, false);

        UserProfile saved = UserProfile.createUserProfile(
                "kc-123",
                "a@b.com",
                "홍길동",
                "user_unique"
        );

        given(userRepository.save(any(UserProfile.class)))
                .willReturn(saved);

        // when
        UserProfileResponse result = userProfileService.loadOrCreateFromToken(jwt);

        // then
        assertThat(result).isNotNull();
        then(userRepository).should(times(3)).existsByNickname(anyString());
        then(userRepository).should().save(any(UserProfile.class));
    }
}
