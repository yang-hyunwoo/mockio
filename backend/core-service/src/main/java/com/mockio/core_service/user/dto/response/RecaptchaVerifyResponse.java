package com.mockio.core_service.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecaptchaVerifyResponse(
        boolean success,
        String challenge_ts,
        String hostname,
        @JsonProperty("error-codes")
        List<String> errorCodes
) {}
