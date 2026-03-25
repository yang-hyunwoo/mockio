package com.mockio.support_service.file.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mockio.common_core.exception.CustomApiException;
import com.mockio.support_service.file.domain.FileDetail;
import com.mockio.support_service.file.domain.FileGroup;
import com.mockio.support_service.file.dto.response.FileUploadResponse;
import com.mockio.support_service.file.dto.response.UserProfileImageResponse;
import com.mockio.support_service.file.repository.FileDetailRepository;
import com.mockio.support_service.file.repository.FileGroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static com.mockio.common_core.constant.CommonErrorEnum.ERR_012;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final FileGroupRepository fileGroupRepository;
    private final FileDetailRepository fileDetailRepository;
    private final CloudinaryService cloudinaryService;
    private final S3Presigner s3Presigner;
    private final S3Client r2Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    public FileUploadResponse upload(String domainType,
                                     Long domainId,
                                     MultipartFile file,
                                     Long profileImageId
    ) {
        FileGroup fileGroup;
        if (profileImageId == null) {
            fileGroup = fileGroupRepository.save(FileGroup.createFileGroup(
                    domainType,
                    domainId,
                    false
            ));
        } else {
            fileGroup = getFileGroupDetail(profileImageId, domainId, domainType);
        }
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image/")) {
            if (domainType.equals("USER")) {
                List<Long> delFileDetailIdList = fileGroup.getFileDetails().stream()
                        .filter(fileDetail -> !fileDetail.isDeleted())
                        .map(FileDetail::getId)
                        .toList();
                deleteFileDetail(delFileDetailIdList);
            }
            uploadImageCloudinary(fileGroup, file);
        } else {
            uploadEtcCloudflare(fileGroup, file);
        }
        return new FileUploadResponse(
                fileGroup.getId()
        );
    }

    public UserProfileImageResponse getProfileImageId(String domainType,
                                                      Long userId,
                                                      Long profileImageId) {
         getFileGroupDetail(profileImageId, userId, domainType);
        FileDetail fileDetail = fileDetailRepository.findTopByFileGroupIdAndDeletedFalseOrderByIdDesc(profileImageId)
                .orElseThrow(
                        () -> new CustomApiException(ERR_012.getHttpStatus(), ERR_012, ERR_012.getMessage()));
        return new UserProfileImageResponse(fileDetail.getFileUrl());
    }

    public void uploadImageCloudinary(FileGroup group,
                                      MultipartFile file
    ) {
        Cloudinary cloudinary = cloudinaryService.connectCloudinary();
        String fileUrl;
        try {
            File tempFile = File.createTempFile("temp-", file.getOriginalFilename());
            file.transferTo(tempFile);
            String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            var result = cloudinary.uploader().upload(
                    tempFile,
                    ObjectUtils.asMap("public_id", "myfolder/" + uniqueName,
                            "overwrite", false
                    )
            );
            tempFile.delete();
            fileUrl = result.get("secure_url").toString();
            String publicId = result.get("public_id").toString();
            FileDetail filesDetails = FileDetail.createFileDetail(file.getOriginalFilename(),
                    uniqueName,
                    fileUrl ,
                    file.getSize() ,
                    file.getContentType() ,
                    "cloudinary",
                    publicId ,
                    false);
            group.addFileDetail(filesDetails);
            fileDetailRepository.save(filesDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * cloudflare 업로드
     * @param group
     * @param file
     */
    public void uploadEtcCloudflare(FileGroup group,
                                    MultipartFile file
    ) {
        try {
            String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            r2Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(uniqueName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
            FileDetail filesDetails = FileDetail.createFileDetail(file.getOriginalFilename(),
                    uniqueName,
                    "https://" + bucketName + "." + "42ff437d4dbd734cbcd48e9fd2156fbc" + ".r2.cloudflarestorage.com/" + uniqueName ,
                    file.getSize() ,
                    file.getContentType() ,
                    "cloudflare" ,
                    null,
                    false);
            filesDetails.linkToFiles(group);
            group.addFileDetail(filesDetails);
            fileDetailRepository.save(filesDetails);
            // R2는 public endpoint 별도 세팅해야 함
        } catch (IOException e) {
            throw new RuntimeException("Cloudflare R2 업로드 실패", e);
        }
    }

    /**
     * 첨부파일 soft-delete
     * @param delFileDetailsId
     */
    public void deleteFileDetail(List<Long> delFileDetailsId
    ) {
        for (Long fileDetailId : delFileDetailsId) {
            fileDetailRepository.findById(fileDetailId).ifPresent(FileDetail::updateFileDeleted);
        }
    }

    public String generateSignedUrl(String key,
                                    Duration expiresIn
    ) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .responseContentDisposition("attachment; filename=\"" + extractFilename(key).split("_")[1] + "\"")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiresIn)
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

    private FileGroup getFileGroupDetail(Long profileImageId, Long domainId, String domainType) {
        return fileGroupRepository.findByIdAndDomainIdAndDomainType(
                profileImageId,
                domainId,
                domainType
        ).orElseThrow(
                () -> new CustomApiException(ERR_012.getHttpStatus(), ERR_012, ERR_012.getMessage())
        );
    }

    private String extractFilename(String key) {
        int lastSlash = key.lastIndexOf("/");
        return (lastSlash != -1) ? key.substring(lastSlash + 1) : key;
    }

}
