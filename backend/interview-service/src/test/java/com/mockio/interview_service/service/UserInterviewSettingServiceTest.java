package com.mockio.interview_service.service;

import com.mockio.common_spring.exception.CustomApiException;
import com.mockio.interview_service.Mapper.UserInterviewSettingMapper;
import com.mockio.interview_service.domain.UserInterviewSetting;
import com.mockio.interview_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.interview_service.dto.request.UserInterviewSettingUpdateRequest;
import com.mockio.interview_service.dto.response.UserInterviewSettingReadResponse;
import com.mockio.interview_service.repository.UserInterviewSettingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.mockio.common_spring.constant.CommonErrorEnum.ERR_012;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInterviewSettingServiceTest {

    @Mock
    private UserInterviewSettingRepository repository;

    // @InjectMocks 대신 명시적으로 생성해도 됨
    private UserInterviewSettingService service;

    UserInterviewSettingServiceTest() {
    }

    private UserInterviewSettingService createService() {
        return new UserInterviewSettingService(repository);
    }

    @Test
    @DisplayName("ensureInterviewSettingSave: 최초 저장이면 save가 호출된다")
    void ensureInterviewSettingSave_success() {
        // given
        service = createService();
        EnsureInterviewSettingRequest req = new EnsureInterviewSettingRequest("kc-1");

        when(repository.save(any(UserInterviewSetting.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        service.ensureInterviewSettingSave(req);

        // then
        verify(repository, times(1)).save(any(UserInterviewSetting.class));
    }

    @Test
    @DisplayName("ensureInterviewSettingSave: 이미 존재하여 DataIntegrityViolationException 발생해도 예외 없이 종료된다")
    void ensureInterviewSettingSave_duplicate_ignored() {
        // given
        service = createService();
        EnsureInterviewSettingRequest req = new EnsureInterviewSettingRequest("kc-1");

        when(repository.save(any(UserInterviewSetting.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        // when & then
        assertThatCode(() -> service.ensureInterviewSettingSave(req))
                .doesNotThrowAnyException();

        verify(repository, times(1)).save(any(UserInterviewSetting.class));
    }

    @Test
    @DisplayName("getPreference: 존재하면 mapper 결과를 반환한다")
    void getPreference_success() {
        // given
        service = createService();
        String keycloakId = "kc-1";

        UserInterviewSetting entity = mock(UserInterviewSetting.class);
        when(repository.findByUserId(keycloakId)).thenReturn(Optional.of(entity));

        UserInterviewSettingReadResponse expected = mock(UserInterviewSettingReadResponse.class);

        // static mapper mocking
        try (MockedStatic<UserInterviewSettingMapper> mocked = mockStatic(UserInterviewSettingMapper.class)) {
            mocked.when(() -> UserInterviewSettingMapper.from(entity)).thenReturn(expected);

            // when
            UserInterviewSettingReadResponse result = service.getPreference(keycloakId);

            // then
            assertThat(result).isSameAs(expected);
            verify(repository, times(1)).findByUserId(keycloakId);
            mocked.verify(() -> UserInterviewSettingMapper.from(entity), times(1));
        }
    }

    @Test
    @DisplayName("getPreference: 없으면 CustomApiException(404, ERR_012) 발생")
    void getPreference_notFound() {
        // given
        service = createService();
        String keycloakId = "kc-not-found";
        when(repository.findByUserId(keycloakId)).thenReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> service.getPreference(keycloakId));

        // then
        assertThat(t).isInstanceOf(CustomApiException.class);
        CustomApiException ex = (CustomApiException) t;

        // CustomApiException의 getter가 무엇인지 프로젝트에 따라 다릅니다.
        // 아래는 흔한 케이스를 가정한 예시입니다. (필드/메서드명에 맞게 수정)
        assertThat(ex.getStatus()).isEqualTo(404);
        assertThat(ex.getErrorEnum()).isEqualTo(ERR_012);

        verify(repository, times(1)).findByUserId(keycloakId);
    }

    @Test
    @DisplayName("updatePreference: 존재하면 엔티티 applyPatch가 호출된다")
    void updatePreference_success() {
        // given
        service = createService();
        String keycloakId = "kc-1";

        UserInterviewSetting entity = mock(UserInterviewSetting.class);
        when(repository.findByUserId(keycloakId)).thenReturn(Optional.of(entity));

        UserInterviewSettingUpdateRequest req = mock(UserInterviewSettingUpdateRequest.class);
        // req.track(), req.difficulty() ... 값을 꺼내서 넘기므로 stub 필요
        when(req.track()).thenReturn(null);
        when(req.difficulty()).thenReturn(null);
        when(req.feedbackStyle()).thenReturn(null);
        when(req.interviewMode()).thenReturn(null);
        when(req.answerTimeSeconds()).thenReturn(null);

        // when
        service.updatePreference(keycloakId, req);

        // then
        verify(repository, times(1)).findByUserId(keycloakId);
        verify(entity, times(1)).applyPatch(
                req.track(),
                req.difficulty(),
                req.feedbackStyle(),
                req.interviewMode(),
                req.answerTimeSeconds(),
                req.interviewQuestionCount()
        );
        // save 호출이 없어도 정상 (JPA dirty checking 전제)
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("updatePreference: 없으면 CustomApiException(404, ERR_012) 발생")
    void updatePreference_notFound() {
        // given
        service = createService();
        String keycloakId = "kc-not-found";
        when(repository.findByUserId(keycloakId)).thenReturn(Optional.empty());

        UserInterviewSettingUpdateRequest req = mock(UserInterviewSettingUpdateRequest.class);

        // when
        Throwable t = catchThrowable(() -> service.updatePreference(keycloakId, req));

        // then
        assertThat(t).isInstanceOf(CustomApiException.class);
        CustomApiException ex = (CustomApiException) t;
        assertThat(ex.getStatus()).isEqualTo(404);
        assertThat(ex.getErrorEnum()).isEqualTo(ERR_012);

        verify(repository, times(1)).findByUserId(keycloakId);
    }
}