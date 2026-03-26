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
                    UserErrorEnum.RECAPTCHA_REQUIRED.getHttpStatus(),
                    UserErrorEnum.RECAPTCHA_REQUIRED,
                    UserErrorEnum.RECAPTCHA_REQUIRED.getMessage()
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
                    UserErrorEnum.RECAPTCHA_VERIFY_FAILED.getHttpStatus(),
                    UserErrorEnum.RECAPTCHA_VERIFY_FAILED,
                    UserErrorEnum.RECAPTCHA_VERIFY_FAILED.getMessage()
            );
        }

        if (response == null || !response.success()) {
            throw new CustomApiException(
                    UserErrorEnum.RECAPTCHA_INVALID.getHttpStatus(),
                    UserErrorEnum.RECAPTCHA_INVALID,
                    UserErrorEnum.RECAPTCHA_INVALID.getMessage()
            );
        }
    }

}