package com.mockio.auth_service.config;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEndpointsController {

    @GetMapping(value = "/api/auth/v1/naver/userinfo", produces = MediaType.TEXT_PLAIN_VALUE)
    public String userinfo() {
        return "userinfo-ok";
    }

    @GetMapping(value = "/api/auth/v1/other", produces = MediaType.TEXT_PLAIN_VALUE)
    public String other() {
        return "other-ok";
    }
}
