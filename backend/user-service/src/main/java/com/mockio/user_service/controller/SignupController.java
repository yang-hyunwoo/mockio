package com.mockio.user_service.controller;


import com.mockio.common_spring.util.MessageUtil;
import com.mockio.common_spring.util.response.Response;
import com.mockio.user_service.dto.request.SignupRequest;
import com.mockio.user_service.dto.response.SignupResponse;
import com.mockio.user_service.service.UserService;
import com.mockio.user_service.util.CustomCookie;
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

    @PostMapping("/signup")
    public ResponseEntity<Response<SignupResponse>> signup(
            @RequestBody SignupRequest request
    ) {
        SignupResponse join = userService.join(request);
        ResponseCookie cookie = CustomCookie.createCookie("join_success", "success", 30);
        return Response.create(cookie.toString(),messageUtil.getMessage("response.create"), join);
    }

}
