package com.mockio.common_spring.exception;

import com.mockio.common_core.exception.RefreshTokenMissingException;
import com.mockio.common_core.constant.CommonErrorEnum;
import com.mockio.common_spring.util.MessageUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExceptionTestController.class)
@Import(CustomExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MessageUtil messageUtil;

    @Test
    @DisplayName("CustomApiException 발생 시 400 에러 응답 스펙을 반환한다")
    void shouldReturnBadRequestWhenCustomApiExceptionOccurs() throws Exception {
        mockMvc.perform(get("/ex/custom"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.httpCode").value(400))
                .andExpect(jsonPath("$.message").value("커스텀 에러"))
                .andExpect(jsonPath("$.errCode").value("NOT_VALIDATION"))
                .andExpect(jsonPath("$.errCodeMsg").value("유효성 검사에 실패했습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 경로 요청 시 404 에러 응답을 반환한다")
    void shouldReturnNotFoundWhenRequestPathDoesNotExist() throws Exception {
        Mockito.when(messageUtil.getMessage("error.not.fount.ok"))
                .thenReturn("not found");

        mockMvc.perform(get("/does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.httpCode").value(404))
                .andExpect(jsonPath("$.message").value("not found"))
                .andExpect(jsonPath("$.errCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.errCodeMsg").value("존재하지 않는 경로입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("허용되지 않은 HTTP 메서드 요청 시 405 에러를 반환한다")
    void shouldReturnMethodNotAllowedWhenHttpMethodIsNotSupported() throws Exception {
        Mockito.when(messageUtil.getMessage("error.not.allow.method.ok"))
                .thenReturn("method not allowed");

        mockMvc.perform(post("/ex/get-only"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.httpCode").value(405))
                .andExpect(jsonPath("$.message").value("method not allowed"))
                .andExpect(jsonPath("$.errCode").value("NOT_ALLOW"))
                .andExpect(jsonPath("$.errCodeMsg").value("허용되지 않은 HTTP 메서드입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("RefreshToken이 없는 경우 401 에러를 반환한다")
    void shouldReturnUnauthorizedWhenRefreshTokenIsMissing()  throws Exception {
        Mockito.when(messageUtil.getMessage("error.refresh.missing"))
                .thenReturn("refresh missing");

        mockMvc.perform(get("/ex/refresh-missing"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.httpCode").value(401))
                .andExpect(jsonPath("$.message").value("refresh missing"))
                .andExpect(jsonPath("$.errCode").value("ERR_TOKEN_MISSING"))
                .andExpect(jsonPath("$.errCodeMsg").value("리프레시 토큰이 없습니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}

@RestController
class ExceptionTestController {

    @GetMapping("/ex/custom")
    public void custom() {
        throw new com.mockio.common_core.exception.CustomApiException(
                HttpStatus.BAD_REQUEST.value(),
                CommonErrorEnum.ERR_002,
                "커스텀 에러"
        );
    }

    @GetMapping("/ex/get-only")
    public String getOnly() {
        return "ok";
    }

    @GetMapping("/ex/refresh-missing")
    public void refreshMissing() {
        throw new RefreshTokenMissingException("refresh missing");
    }
}