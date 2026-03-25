package com.mockio.support_service.file.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mockio.common_core.exception.CustomApiException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.mockio.support_service.file.constant.FileErrorCode.CLOUDINARY_ERROR;
import static com.mockio.support_service.file.constant.FileErrorCode.CLOUDINARY_NOT_CONNECT;


@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    /**
     * 애플리케이션 기동 시 Cloudinary 연결 테스트 (샘플 업로드 → 삭제)
     */
    @PostConstruct
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));

        try {
            // 샘플 이미지 업로드 (Cloudinary 공식 샘플 URL)
            Map uploadResult = cloudinary.uploader().upload(
                    "https://res.cloudinary.com/demo/image/upload/sample.jpg",
                    ObjectUtils.emptyMap()
            );

            String publicId = uploadResult.get("public_id").toString();
            log.info("Cloudinary 연결 성공: uploaded sample file public_id = {}", publicId);
            // 업로드 직후 삭제
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        } catch (IOException e) {
            log.error("❌ Cloudinary 네트워크 연결 실패: {}", e.getMessage(), e);
            throw new CustomApiException(CLOUDINARY_NOT_CONNECT.getHttpStatus(), CLOUDINARY_NOT_CONNECT, CLOUDINARY_NOT_CONNECT.getMessage());
        } catch (Exception e) {
            log.error("❌ Cloudinary 알 수 없는 오류: {}", e.getMessage(), e);
            throw new CustomApiException(CLOUDINARY_ERROR.getHttpStatus(), CLOUDINARY_ERROR,CLOUDINARY_ERROR.getMessage());
        }
    }

    public Cloudinary connectCloudinary() {
        return cloudinary;
    }
}
