package com.mockio.user_service.controller;

import com.mockio.common_security.annotation.CurrentUser;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.user_service.domain.UserProfile;
import com.mockio.user_service.dto.request.UserInterviewPreferenceUpdateRequest;
import com.mockio.user_service.dto.response.UserInterviewPreferenceReadResponse;
import com.mockio.user_service.service.UserInterviewPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserProfileController.
 *
 *  면접 설정 관련 API를 제공합니다.
 */

@RestController
@RequestMapping("/api/users/v1")
@RequiredArgsConstructor
public class UserInterviewPreferenceController {

    private final UserInterviewPreferenceService userInterviewPreferenceService;
    private final MessageUtil messageUtil;

    /**
     * 면접 설정 조회
     * @param user
     * @return
     */
    @GetMapping("/me/get-preference")
    public ResponseEntity<Response<UserInterviewPreferenceReadResponse>> getPreference(@CurrentUser UserProfile user) {
        return Response.ok(messageUtil.getMessage("response.read"), userInterviewPreferenceService.getPreference(user.getKeycloakId()));
    }

    /**
     * 면접 설정 수정
     * @param user
     * @param userPreferenceUpdateRequest
     * @return
     */
    @PatchMapping("/me/update-preference")
    public ResponseEntity<Response<Void>> updatePreference(@CurrentUser UserProfile user ,
                                                           @RequestBody UserInterviewPreferenceUpdateRequest userPreferenceUpdateRequest) {
        userInterviewPreferenceService.updatePreference(user.getKeycloakId(), userPreferenceUpdateRequest);

        return Response.update(messageUtil.getMessage("response.update"));

    }



}
