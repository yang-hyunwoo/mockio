package com.mockio.user_service.service;

import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.user_service.constant.FeedbackStyle;
import com.mockio.user_service.constant.InterviewDifficulty;
import com.mockio.user_service.constant.InterviewMode;
import com.mockio.user_service.constant.InterviewTrack;
import com.mockio.user_service.domain.UserInterviewPreference;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.request.UserInterviewPreferenceUpdateRequest;
import com.mockio.user_service.dto.response.UserInterviewPreferenceReadResponse;
import com.mockio.user_service.repository.UserInterviewPreferenceRepository;
import com.mockio.user_service.repository.UserProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserInterviewPreferenceServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserInterviewPreferenceRepository userInterviewPreferenceRepository;

    @InjectMocks
    private UserInterviewPreferenceService userInterviewPreferenceService;

    @Test
    @DisplayName("getPreference: 정상적으로 설정을 조회하여 ReadResponse를 반환한다")
    void getPreference_success_returnsReadResponse() {
        // given
        String keycloakId = "kc-123";

        UserProfile userProfile = mock(UserProfile.class);
        given(userProfile.getId()).willReturn(10L);

        UserInterviewPreference preference = mock(UserInterviewPreference.class);

        given(userProfileRepository.findByKeycloakId(keycloakId)).willReturn(Optional.of(userProfile));
        given(userInterviewPreferenceRepository.findById(10L)).willReturn(Optional.of(preference));
        given(preference.getTrack()).willReturn(InterviewTrack.GENERAL);
        given(preference.getDifficulty()).willReturn(InterviewDifficulty.EASY);
        given(preference.getFeedbackStyle()).willReturn(FeedbackStyle.COACHING);
        given(preference.getInterviewMode()).willReturn(InterviewMode.TEXT);
        given(preference.getAnswerTimeSeconds()).willReturn(60);

        // when
        UserInterviewPreferenceReadResponse res = userInterviewPreferenceService.getPreference(keycloakId);

        // then
        assertThat(res).isNotNull();

        then(userProfileRepository).should(times(1)).findByKeycloakId(keycloakId);
        then(userInterviewPreferenceRepository).should(times(1)).findById(10L);
    }

    @Test
    @DisplayName("getPreference: keycloakId로 유저를 찾지 못하면 CustomApiException이 발생한다")
    void getPreference_userNotFound_throwsCustomApiException() {
        // given
        String keycloakId = "kc-not-exist";
        given(userProfileRepository.findByKeycloakId(keycloakId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userInterviewPreferenceService.getPreference(keycloakId))
                .isInstanceOf(CustomApiException.class);

        then(userInterviewPreferenceRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("getPreference: preference를 찾지 못하면 CustomApiException이 발생한다")
    void getPreference_preferenceNotFound_throwsCustomApiException() {
        // given
        String keycloakId = "kc-123";

        UserProfile userProfile = mock(UserProfile.class);
        given(userProfile.getId()).willReturn(10L);

        given(userProfileRepository.findByKeycloakId(keycloakId)).willReturn(Optional.of(userProfile));
        given(userInterviewPreferenceRepository.findById(10L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userInterviewPreferenceService.getPreference(keycloakId))
                .isInstanceOf(CustomApiException.class);

        then(userInterviewPreferenceRepository).should(times(1)).findById(10L);
    }


    @Test
    @DisplayName("updatePreference: 정상적으로 preference.update가 요청 값대로 호출된다")
    void updatePreference_success_callsUpdateWithRequestValues() {
        // given
        String keycloakId = "kc-123";

        UserProfile userProfile = mock(UserProfile.class);
        given(userProfile.getId()).willReturn(10L);

        UserInterviewPreference preference = mock(UserInterviewPreference.class);

        UserInterviewPreferenceUpdateRequest req = mock(UserInterviewPreferenceUpdateRequest.class);
        given(req.track()).willReturn(null);
        given(req.difficulty()).willReturn(null);
        given(req.feedbackStyle()).willReturn(null);
        given(req.interviewMode()).willReturn(null);
        given(req.answerTimeSeconds()).willReturn(60);

        given(userProfileRepository.findByKeycloakId(keycloakId)).willReturn(Optional.of(userProfile));
        given(userInterviewPreferenceRepository.findById(10L)).willReturn(Optional.of(preference));

        // when
        userInterviewPreferenceService.updatePreference(keycloakId, req);

        // then
        then(preference).should(times(1)).update(
                req.track(),
                req.difficulty(),
                req.feedbackStyle(),
                req.interviewMode(),
                req.answerTimeSeconds()
        );
        then(userInterviewPreferenceRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("updatePreference: keycloakId로 유저를 찾지 못하면 CustomApiException(NOT_FOUND, ERR_012)이 발생한다")
    void updatePreference_userNotFound_throwsCustomApiException() {
        // given
        String keycloakId = "kc-not-exist";
        UserInterviewPreferenceUpdateRequest req = mock(UserInterviewPreferenceUpdateRequest.class);

        given(userProfileRepository.findByKeycloakId(keycloakId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userInterviewPreferenceService.updatePreference(keycloakId, req))
                .isInstanceOf(CustomApiException.class);

        then(userInterviewPreferenceRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("updatePreference: preference를 찾지 못하면 CustomApiException(NOT_FOUND, ERR_012)이 발생한다")
    void updatePreference_preferenceNotFound_throwsCustomApiException() {
        // given
        String keycloakId = "kc-123";

        UserProfile userProfile = mock(UserProfile.class);
        given(userProfile.getId()).willReturn(10L);

        UserInterviewPreferenceUpdateRequest req = mock(UserInterviewPreferenceUpdateRequest.class);

        given(userProfileRepository.findByKeycloakId(keycloakId)).willReturn(Optional.of(userProfile));
        given(userInterviewPreferenceRepository.findById(10L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userInterviewPreferenceService.updatePreference(keycloakId, req))
                .isInstanceOf(CustomApiException.class);

        then(userInterviewPreferenceRepository).should(times(1)).findById(10L);
    }




}