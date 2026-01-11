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
import com.mockio.common_spring.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

import static com.mockio.ai_service.constant.AIErrorEnum.ILLEGALSTATE;
import static org.springframework.http.HttpStatus.*;
import static reactor.core.publisher.Mono.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAIClient {

    private final WebClient webClient;

    public String chat(String model, String prompt) {
        OpenAIChatRequest req = new OpenAIChatRequest(
                model,
                List.of(new OpenAIChatRequest.Message("system",
                                "당신은 기술면접관입니다. 사용자의 요청 형식(한 줄에 질문 하나, 번호/설명 금지)을 반드시 지키세요."),
                        new OpenAIChatRequest.Message("user", prompt)
                ),
                0.7
        );

        OpenAIChatResponse res = webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> {
                                    int code = r.statusCode().value();

                                    AIErrorEnum err = switch (code) {
                                        case 400 -> AIErrorEnum.BAD_REQUEST;
                                        case 401, 403 -> AIErrorEnum.UNAUTHORIZED;
                                        case 429 -> AIErrorEnum.RATE_LIMIT;
                                        default -> AIErrorEnum.ILLEGALSTATE; // 5xx 포함
                                    };
                                    String shortBody = body.length() > 300 ? body.substring(0, 300) + "...(truncated)" : body;
                                    log.warn("OpenAI error status={} body={}", code, shortBody);
                                    return new CustomApiException(code, err, err.getMessage());
                                })
                                .flatMap(ex -> error(ex))
                )
                .bodyToMono(OpenAIChatResponse.class)
                .timeout(Duration.ofSeconds(15))
                .block();

        if (res == null || res.choices() == null || res.choices().isEmpty()
                || res.choices().get(0).message() == null
                || res.choices().get(0).message().content() == null) {
            throw new CustomApiException(INTERNAL_SERVER_ERROR.value(),ILLEGALSTATE, ILLEGALSTATE.getMessage());
        }
        return res.choices().get(0).message().content();
    }
}
