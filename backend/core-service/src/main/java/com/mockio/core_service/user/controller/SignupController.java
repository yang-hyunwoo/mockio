package com.mockio.core_service.user.controller;

import com.mockio.common_spring.util.CustomCookie;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.user.dto.request.SignupRequest;
import com.mockio.core_service.user.dto.response.SignupResponse;
import com.mockio.core_service.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/v1/public")
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;
    private final MessageUtil messageUtil;
    private final CustomCookie customCookie;

    /**
     * 사용자 회원 가입
     *
     * @param request
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<Response<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse join = userService.join(request);
        ResponseCookie cookie = customCookie.createCookie("join_success", "success", 30);
        return Response.create(cookie.toString(), messageUtil.getMessage("response.create"), join);
    }

}
