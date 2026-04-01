package com.mockio.core_service.user.service;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.user.constant.error.UserErrorEnum;
import com.mockio.core_service.user.dto.response.RecaptchaVerifyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static com.mockio.core_service.user.constant.error.UserErrorEnum.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecaptchaService {

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;
    private final RestClient recaptchaRestClient;

    private static final String VERIFY_URL = "recaptcha/api/siteverify";

    public void verify(String token) {
        if (token == null || token.isBlank()) {
            throw new CustomApiException(
                    RECAPTCHA_REQUIRED.getHttpStatus(),
                    RECAPTCHA_REQUIRED,
                    RECAPTCHA_REQUIRED.getMessage()
            );
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", recaptchaSecret);
        formData.add("response", token);

        RecaptchaVerifyResponse response;

        try {
            response = recaptchaRestClient.post()
                    .uri(VERIFY_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(RecaptchaVerifyResponse.class);
        } catch (Exception e) {
            throw new CustomApiException(
                    RECAPTCHA_VERIFY_FAILED.getHttpStatus(),
                    RECAPTCHA_VERIFY_FAILED,
                    RECAPTCHA_VERIFY_FAILED.getMessage()
            );
        }

        if (response == null || !response.success()) {
            throw new CustomApiException(
                    RECAPTCHA_INVALID.getHttpStatus(),
                    RECAPTCHA_INVALID,
                    RECAPTCHA_INVALID.getMessage()
            );
        }
    }

}