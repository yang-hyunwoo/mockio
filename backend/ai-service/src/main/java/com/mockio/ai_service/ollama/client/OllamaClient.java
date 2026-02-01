package com.mockio.ai_service.ollama.client;

import com.mockio.ai_service.constant.AIErrorEnum;
import com.mockio.ai_service.ollama.dto.request.OllamaChatRequest;
import com.mockio.ai_service.ollama.dto.response.OllamaChatResponse;
import com.mockio.ai_service.util.AIChatClient;
import com.mockio.common_spring.exception.CustomApiException;
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
public class OllamaClient implements AIChatClient {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    private final WebClient webClient;

    @Override
    public String chat(String model, String prompt,String commandText,Double temperature) {
        OllamaChatRequest req = new OllamaChatRequest(
                model,
                List.of(
                        new OllamaChatRequest.Message(
                                "system",
                                commandText
                        ),
                        new OllamaChatRequest.Message("user", prompt)
                ),
                temperature
        );

        OllamaChatResponse res = webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(req)
                .exchangeToMono(this::handleResponse)
                .onErrorMap(this::mapToCustomApiException)
                .block(REQUEST_TIMEOUT);

        if (res == null || res.choices() == null || res.choices().isEmpty()
                || res.choices().get(0).message() == null
                || res.choices().get(0).message().content() == null) {
            throw new CustomApiException(
                    INTERNAL_SERVER_ERROR.value(),
                    ILLEGALSTATE,
                    ILLEGALSTATE.getMessage()
            );
        }

        return res.choices().get(0).message().content();
    }

    private Mono<OllamaChatResponse> handleResponse(ClientResponse response) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(OllamaChatResponse.class);
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

        log.warn("Ollama error status={} body={}", code, shortBody);
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
            log.warn("Ollama request error: {}", t.toString());
            return new CustomApiException(
                    INTERNAL_SERVER_ERROR.value(),
                    ILLEGALSTATE,
                    "외부 AI 서버 연결에 실패했습니다."
            );
        }

        // 타임아웃 계열
        if (isTimeout(t)) {
            log.warn("Ollama timeout: {}", t.toString());
            return new CustomApiException(
                    INTERNAL_SERVER_ERROR.value(),
                    ILLEGALSTATE,
                    "외부 AI 서버 응답이 지연되었습니다."
            );
        }

        // 그 외(역직렬화 등)
        log.warn("Ollama unexpected error: {}", t.toString());
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
