package com.mockio.support_service.comment.client;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.common_spring.util.APIErrorResponse;
import com.mockio.support_service.questionboard.constant.QuestionboardErrorEnum;
import com.mockio.support_service.questionboard.dto.internal.response.InternalQuestionBoardDetailResponse;
import com.mockio.support_service.questionboard.dto.internal.response.InterviewListResponse;
import com.mockio.support_service.questionboard.dto.internal.response.QuestionAnswerResponse;
import com.mockio.support_service.questionboard.dto.internal.response.UserInfoResponse;
import com.mockio.support_service.questionboard.dto.request.InternalQuestionBoardCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoreToCommentClient {

    private final RestClient coreServiceRestClient;


    /**
     * 사용자 정보 조회
     * @param userId
     * @return
     */
    public UserInfoResponse userDetail(Long userId) {
        return coreServiceRestClient.get()
                .uri("/api/users/v1/internal/user-info/{userId}", userId)
                .exchange((request, response) -> {
                    clientError(response);
                    return Objects.requireNonNull(response.bodyTo(UserInfoResponse.class));
                });
    }


    private void clientError(ConvertibleClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            APIErrorResponse error = response.bodyTo(APIErrorResponse.class);
            throw new CustomApiException(
                    error.httpCode() != null
                            ? error.httpCode()
                            : response.getStatusCode().value(),
                    mapErrorCode(error.errCode()),
                    error.message() != null
                            ? error.message()
                            : error.errCodeMsg()
            );
        }
    }

    /**
     * 문자열 에러 코드를 Enum으로 변환
     *
     * - null / 빈값 → ILLEGAL_STATE
     * - 존재하지 않는 코드 → ILLEGAL_STATE
     */
    private QuestionboardErrorEnum mapErrorCode(String errCode) {
        if (errCode == null || errCode.isBlank()) {
            return QuestionboardErrorEnum.ILLEGAL_STATE;
        }
        try {
            return QuestionboardErrorEnum.valueOf(errCode);
        } catch (IllegalArgumentException e) {
            return QuestionboardErrorEnum.ILLEGAL_STATE;
        }
    }

}
