package com.mockio.user_service.controller;

/**
 * testController.
 *
 * 회원 가입, 로그인, 마이페이지 조회 등 유저 관련 API를 제공합니다.
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class testController {

    @GetMapping("/test")
    public String test() {
        return "asdf";
    }

}
