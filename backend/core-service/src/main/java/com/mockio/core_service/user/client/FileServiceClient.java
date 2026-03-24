package com.mockio.core_service.user.client;

import com.mockio.core_service.user.dto.response.FileUploadResponse;
import com.mockio.core_service.user.dto.response.UserProfileImageResponse;
import com.mockio.core_service.user.util.MultipartFileResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
public class FileServiceClient {

    private final RestClient fileServiceRestClient;

    public FileServiceClient(RestClient fileServiceRestClient) {
        this.fileServiceRestClient = fileServiceRestClient;
    }

    public FileUploadResponse upload(MultipartFile file, Long profileImageId, Long userId) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 파일
        body.add("file", new MultipartFileResource(file));

        // 기존 이미지 id (optional)
        if (profileImageId != null) {
            body.add("profileImageId", profileImageId);
        }
        body.add("domainType", "USER");
        body.add("domainId", userId);

        return fileServiceRestClient.post()
                .uri("/api/file/v1/internal/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(FileUploadResponse.class);
    }

    public UserProfileImageResponse getProfileImage(Long userId , Long profileImageId) {
        try {
            return fileServiceRestClient.get()
                    .uri("/api/file/v1/internal/{domainType}/{userId}/{profileImageId}"
                            , "USER", userId, profileImageId)
                    .retrieve()
                    .body(UserProfileImageResponse.class);
        } catch (RestClientException ex) {
            log.error("file-service getProfileImage 호출 실패. userId={}", userId, ex);
            throw new IllegalStateException("file-service 호출 실패", ex);
        }
    }
}
