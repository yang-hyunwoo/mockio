package com.mockio.core_service.interview.service;

/**
 * UserInterviewPreferenceService.
 *
 *  면접 설정 관련 서비스를 제공합니다.
 */

import com.mockio.core_service.interview.Mapper.UserInterviewSettingMapper;
import com.mockio.core_service.interview.domain.UserInterviewSetting;
import com.mockio.core_service.interview.dto.request.InternalEnsureInterviewSettingRequest;
import com.mockio.core_service.interview.dto.request.UserInterviewSettingUpdateRequest;
import com.mockio.core_service.interview.dto.response.InterviewUserInterviewSettingReadResponse;
import com.mockio.core_service.interview.repository.UserInterviewSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserInterviewSettingService {

    private final UserInterviewSettingRepository userInterviewSettingRepository;

    /**
     * 초기 면접 설정 저장
     */
    public void ensureInterviewSettingSave(InternalEnsureInterviewSettingRequest request) {
        UserInterviewSetting setting =
                UserInterviewSetting.createUserInterviewPreference(request.userId());

        userInterviewSettingRepository.insertIfAbsent(
                setting.getUserId(),
                setting.getTrack().name(),
                setting.getDifficulty().name(),
                setting.getFeedbackStyle().name(),
                setting.getInterviewMode().name(),
                setting.getAnswerTimeSeconds(),
                setting.getInterviewQuestionCount()
        );
    }

    /**
     * 유저 면접 업을 경우 생성 메서드
     * @param userId
     * @return
     */
    public UserInterviewSetting absentEnsureSettingSave(Long userId) {
        UserInterviewSetting setting = UserInterviewSetting.createUserInterviewPreference(userId);

        userInterviewSettingRepository.insertIfAbsent(
                setting.getUserId(),
                setting.getTrack().name(),
                setting.getDifficulty().name(),
                setting.getFeedbackStyle().name(),
                setting.getInterviewMode().name(),
                setting.getAnswerTimeSeconds(),
                setting.getInterviewQuestionCount()
        );
        return setting;
    }

    /**
     * 면접 설정 조회
     * @param userId
     * @return
     */
    public InterviewUserInterviewSettingReadResponse getPreference(Long userId) {
        return UserInterviewSettingMapper.from(findByUserId(userId));
    }

    /**
     * 면접 설정 수정
     *
     * @param userId
     * @param userRequest
     */
    public void updatePreference(Long userId, UserInterviewSettingUpdateRequest userRequest) {
        UserInterviewSetting byUserId = findByUserId(userId);
        byUserId.applyPatch(userRequest.track(),
                userRequest.difficulty(),
                userRequest.feedbackStyle(),
                userRequest.interviewMode(),
                userRequest.answerTimeSeconds(),
                userRequest.questionCount()
        );
    }

    /**
     * 면접 질문 테이블 조회
     * @param userId
     * @return
     */
    private UserInterviewSetting findByUserId(Long userId) {
       return userInterviewSettingRepository.findByUserId(userId)
                .orElseGet(() -> absentEnsureSettingSave(userId) );
    }

}
