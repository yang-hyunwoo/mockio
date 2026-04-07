package com.mockio.core_service.interview.controller;

import com.mockio.common_security.annotation.CurrentSubject;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.interview.dto.request.InternalEnsureInterviewSettingRequest;
import com.mockio.core_service.interview.dto.request.UserInterviewSettingUpdateRequest;
import com.mockio.core_service.interview.dto.response.InterviewUserInterviewSettingReadResponse;
import com.mockio.core_service.interview.service.UserInterviewSettingService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserProfileController.
 *
 *  면접 설정 관련 API를 제공합니다.
 */

@Tag(name = "면접 프로세스")
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
    @Hidden
    @PostMapping("/internal/interview-setting/ensure")
    public ResponseEntity<Response<Void>> ensureInterviewSettingSave(@RequestBody @Valid InternalEnsureInterviewSettingRequest request) {
        userInterviewSettingService.ensureInterviewSettingSave(request);
        return Response.create(messageUtil.getMessage("response.create"), null);
    }

    /**
     * 면접 설정 조회
     *
     * @param
     * @return
     */
    @Operation(summary = "면접 설정 조회")
    @GetMapping("/me/get-preference")
    public ResponseEntity<Response<InterviewUserInterviewSettingReadResponse>> getPreference(
            @CurrentSubject @Parameter(description = "사용자_ID", example = "1") Long userId
    ) {
        return Response.ok(messageUtil.getMessage("response.read"), userInterviewSettingService.getPreference(userId));
    }

    /**
     * 면접 설정 수정
     *
     * @param updateRequest
     * @return
     */
    @Operation(summary = "면접 설정 수정")
    @PatchMapping("/mypage/update-preference")
    public ResponseEntity<Response<Void>> updatePreference(
            @CurrentSubject @Parameter(description = "사용자_ID", example = "1") Long userId,
            @RequestBody @Valid UserInterviewSettingUpdateRequest updateRequest
    ) {
        userInterviewSettingService.updatePreference(userId, updateRequest);
        return Response.update(messageUtil.getMessage("response.update"));
    }

}
