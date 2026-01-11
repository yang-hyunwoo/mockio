package com.mockio.interview_service.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.interview_service.dto.request.EnsureInterviewSettingRequest;
import com.mockio.interview_service.dto.request.UserInterviewSettingUpdateRequest;
import com.mockio.interview_service.dto.response.UserInterviewSettingReadResponse;
import com.mockio.interview_service.service.UserInterviewSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserProfileController.
 *
 *  면접 설정 관련 API를 제공합니다.
 */

@RestController
@RequestMapping("/api/interview/v1")
@RequiredArgsConstructor
public class UserInterviewSettingController {

    private final UserInterviewSettingService userInterviewSettingService;
    private final MessageUtil messageUtil;

    /**
     * 초기 면접 설정 저장
     * @param request
     * @return
     */
    @PostMapping("/interview-setting/ensure")
    public ResponseEntity<Response<Void>> ensureInterviewSettingSave(@CurrentSubject String keycloakId,
                                                                     @RequestBody EnsureInterviewSettingRequest request) {
        userInterviewSettingService.ensureInterviewSettingSave(request);
        return Response.create(messageUtil.getMessage("response.create"), null);

    }

    /**
     * 면접 설정 조회
     * @param
     * @return
     */
    @GetMapping("/me/get-preference")
    public ResponseEntity<Response<UserInterviewSettingReadResponse>> getPreference(@CurrentSubject String keycloakId) {
        return Response.ok(messageUtil.getMessage("response.read"), userInterviewSettingService.getPreference(keycloakId));
    }

    /**
     * 면접 설정 수정
     * @param keycloakId
     * @param updateRequest
     * @return
     */
    @PatchMapping("/me/update-preference")
    public ResponseEntity<Response<Void>> updatePreference(@CurrentSubject String keycloakId ,
                                                           @RequestBody UserInterviewSettingUpdateRequest updateRequest) {
        userInterviewSettingService.updatePreference(keycloakId, updateRequest);

        return Response.update(messageUtil.getMessage("response.update"));

    }

}
