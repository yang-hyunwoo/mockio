package com.mockio.core_service.user.controller;

import com.mockio.common_spring.util.CustomCookie;
import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.core_service.user.dto.request.SignupRequest;
import com.mockio.core_service.user.dto.response.SignupResponse;
import com.mockio.core_service.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입",
        description = """
                회원 가입 관련 API입니다.
                
                - 회원가입
                """
)
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
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<Response<SignupResponse>> signup(
            @Valid @RequestBody SignupRequest request,
            HttpServletRequest servletRequest
    ) {
        SignupResponse join = userService.join(request,servletRequest);
        ResponseCookie cookie = customCookie.createCookie("join_success", "success", 30);
        return Response.create(cookie.toString(), messageUtil.getMessage("response.create"), join);
    }

}
