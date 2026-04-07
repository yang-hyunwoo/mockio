package com.mockio.core_service.ai.openAi.client;

import com.mockio.common_core.exception.CustomApiException;
import com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum;
import com.mockio.core_service.ai.openAi.dto.response.OpenAiSttResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.time.Duration;

import static com.mockio.core_service.ai.constant.errorCode.AIErrorCodeEnum.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAiAudioClient {

    private final WebClient openAiWebClient;

    public String transcribe(InputStream inputStream,
                             String filename,
                             String contentType,
                             String model
    ) {
        validate(inputStream, filename, contentType);

        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();

            builder.part(
                    "file",
                    new MultipartInputStreamFileResource(inputStream, filename)
            ).header("Content-Type", contentType);

            builder.part("model", model);

            OpenAiSttResponse response = openAiWebClient.post()
                    .uri("/v1/audio/transcriptions")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(OpenAiSttResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response == null || response.text() == null || response.text().isBlank()) {
                throw new CustomApiException(
                        AIErrorCodeEnum.AUDIO_TEXT_NULL.getHttpStatus(),
                        AIErrorCodeEnum.AUDIO_TEXT_NULL,
                        AUDIO_TEXT_NULL.getMessage()
                );
            }
            return response.text();

        } catch (CustomApiException e) {
            throw e;
        } catch (Exception e) {
            throw mapToCustomApiException(e);
        }
    }

    private void validate(InputStream inputStream,
                          String filename,
                          String contentType
    ) {
        if (inputStream == null) {
            throw new CustomApiException(
                    AUDIO_EMPTY.getHttpStatus(),
                    AUDIO_EMPTY,
                    AUDIO_EMPTY.getMessage()
            );
        }

        if (!StringUtils.hasText(filename)) {
            throw new CustomApiException(
                    AUDIO_EMPTY.getHttpStatus(),
                    AUDIO_EMPTY,
                    "파일명이 없습니다."
            );
        }

        if (!StringUtils.hasText(contentType)) {
            throw new CustomApiException(
                    AUDIO_TYPE_MISMATCH.getHttpStatus(),
                    AUDIO_TYPE_MISMATCH,
                    AUDIO_TYPE_MISMATCH.getMessage()
            );
        }

        boolean isSupported =
                "audio/webm".equalsIgnoreCase(contentType)
                        || "video/webm".equalsIgnoreCase(contentType)
                        || "audio/wav".equalsIgnoreCase(contentType)
                        || "audio/mpeg".equalsIgnoreCase(contentType)
                        || "audio/mp4".equalsIgnoreCase(contentType);

        if (!isSupported) {
            throw new CustomApiException(
                    AUDIO_TYPE_MISMATCH.getHttpStatus(),
                    AUDIO_TYPE_MISMATCH,
                    "지원하지 않는 오디오 형식입니다. contentType=" + contentType
            );
        }
    }

    private CustomApiException mapToCustomApiException(Throwable t) {
        String msg = t.toString();

        if (msg.contains("429") || msg.toLowerCase().contains("rate limit")) {
            return new CustomApiException(429, RATE_LIMIT, RATE_LIMIT.getMessage());
        }

        if (msg.contains("401") || msg.contains("403")) {
            return new CustomApiException(401, UNAUTHORIZED, UNAUTHORIZED.getMessage());
        }

        if (msg.contains("400")) {
            return new CustomApiException(400, BAD_REQUEST, BAD_REQUEST.getMessage());
        }

        log.warn("OpenAI audio transcription error: {}", t.toString());

        return new CustomApiException(
                500,
                ILLEGAL_STATE,
                "외부 STT 서버 호출 중 오류가 발생했습니다."
        );
    }

    static class MultipartInputStreamFileResource extends InputStreamResource {

        private final String filename;

        public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }

        @Override
        public long contentLength() {
            return -1;
        }
    }

}
