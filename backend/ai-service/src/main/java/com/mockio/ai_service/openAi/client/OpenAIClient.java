package com.mockio.ai_service.openAi.client;

/**
 * OpenAI Chat Completions API 호출을 담당하는 클라이언트 컴포넌트.
 *
 * <p>지정된 모델과 프롬프트를 기반으로 Chat Completion 요청을 생성하고,
 * OpenAI 응답을 도메인에서 사용 가능한 문자열 형태로 변환한다.</p>
 *
 * <p>HTTP 오류 응답은 OpenAI 상태 코드에 따라 AIErrorEnum으로 매핑되며,
 * CustomApiException으로 변환되어 상위 계층에서 일관되게 처리된다.</p>
 *
 * <p>네트워크 지연 및 외부 API 장애에 대비해 타임아웃을 적용하며,
 * 응답 구조가 비정상적인 경우 내부 오류로 처리한다.</p>
 */

import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.ai_service.openAi.dto.request.OpenAIChatRequest;
import com.mockio.ai_service.openAi.dto.response.OpenAIChatResponse;
import com.mockio.ai_service.util.AIChatClient;
import com.mockio.common_core.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.mockio.ai_service.constant.AIErrorEnum.ILLEGALSTATE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAIClient implements AIChatClient {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    private final WebClient openAiWebClient;

    @Override
    public String chat(String model, String prompt,String commandText,Double temperature) {
        OpenAIChatRequest req = new OpenAIChatRequest(
                model,
                List.of(
                        new OpenAIChatRequest.Message(
                                "system",
                                commandText
                        ),
                        new OpenAIChatRequest.Message("user", prompt)
                ),
                temperature
        );

        OpenAIChatResponse res = openAiWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(req)
                .exchangeToMono(this::handleResponse)
                .onErrorMap(this::mapToCustomApiException)
                .block(REQUEST_TIMEOUT);

        if (res == null || res.choices() == null || res.choices().isEmpty()
                || res.choices().getFirst().message() == null
                || res.choices().getFirst().message().content() == null) {
            throw new CustomApiException(
                    INTERNAL_SERVER_ERROR.value(),
                    ILLEGALSTATE,
                    ILLEGALSTATE.getMessage()
            );
        }

        return res.choices().getFirst().message().content();
    }

    private Mono<OpenAIChatResponse> handleResponse(ClientResponse response) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(OpenAIChatResponse.class);
        }

        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> Mono.error(toCustomApiException(response.statusCode().value(), body)));
    }

    private CustomApiException toCustomApiException(int code, String body) {
        AIErrorEnum err = switch (code) {
            case 400 -> AIErrorEnum.BAD_REQUEST;
            case 401, 403 -> AIErrorEnum.UNAUTHORIZED;
            case 429 -> AIErrorEnum.RATE_LIMIT;
            default -> AIErrorEnum.ILLEGALSTATE;
        };

        String shortBody = body == null ? "" :
                (body.length() > 300 ? body.substring(0, 300) + "...(truncated)" : body);

        log.warn("OpenAI error status={} body={}", code, shortBody);
        return new CustomApiException(code, err, err.getMessage());
    }

    private Throwable mapToCustomApiException(Throwable t) {
        if (t instanceof CustomApiException) return t;

        if (t instanceof WebClientResponseException wcre) {
            HttpStatus status = HttpStatus.resolve(wcre.getRawStatusCode());
            HttpStatus resolved = status != null ? status : INTERNAL_SERVER_ERROR;
            return toCustomApiException(resolved.value(), wcre.getResponseBodyAsString());
        }

        // 네트워크/커넥션 계열
        if (t instanceof WebClientRequestException) {
            log.warn("OpenAI request error: {}", t.toString());
            return new CustomApiException(
                    INTERNAL_SERVER_ERROR.value(),
                    ILLEGALSTATE,
                    "외부 AI 서버 연결에 실패했습니다."
            );
        }

        // 타임아웃 계열
        if (isTimeout(t)) {
            log.warn("OpenAI timeout: {}", t.toString());
            return new CustomApiException(
                    INTERNAL_SERVER_ERROR.value(),
                    ILLEGALSTATE,
                    "외부 AI 서버 응답이 지연되었습니다."
            );
        }

        // 그 외(역직렬화 등)
        log.warn("OpenAI unexpected error: {}", t.toString());
        return new CustomApiException(
                INTERNAL_SERVER_ERROR.value(),
                ILLEGALSTATE,
                ILLEGALSTATE.getMessage()
        );
    }

    private boolean isTimeout(Throwable t) {
        if (t instanceof TimeoutException) return true;
        Throwable c = t.getCause();
        if (c instanceof TimeoutException) return true;
        return t.getClass().getName().toLowerCase().contains("timeout")
                || (c != null && c.getClass().getName().toLowerCase().contains("timeout"));
    }

}
