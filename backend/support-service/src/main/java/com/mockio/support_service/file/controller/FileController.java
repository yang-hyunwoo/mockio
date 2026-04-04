package com.mockio.support_service.file.controller;

import com.mockio.support_service.file.dto.response.FileUploadResponse;
import com.mockio.support_service.file.dto.response.UserProfileImageResponse;
import com.mockio.support_service.file.service.FileService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "파일 관리",
        description = """
                파일 관련 API입니다.
                
                - 파일 등록
                - 파일 조회
                """
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file/v1/internal")
public class FileController {

    private final FileService fileService;

    @Hidden
    @Operation(summary = "파일 등록")
    @PostMapping("/files")
    public FileUploadResponse upload(
            @RequestPart("file") @Parameter(description = "파일", example = "file") MultipartFile file,
            @RequestPart(value = "profileImageId", required = false) @Parameter(description = "이미지ID", example = "1") Long profileImageId,
            @RequestPart(value = "domainType") @Parameter(description = "도메인", example = "USER") String domainType,
            @RequestPart(value = "domainId") @Parameter(description = "도메인ID", example = "1") Long domainId
    ) {

        return fileService.upload(domainType,domainId,file, profileImageId);
    }

    @Hidden
    @Operation(summary = "파일 조회")
    @GetMapping("/{domainType}/{userId}/{profileImageId}")
    public UserProfileImageResponse getProfileImageId(
            @PathVariable @Parameter(description = "도메인", example = "USER") String domainType,
            @PathVariable @Parameter(description = "사용자ID", example = "1") Long userId,
            @PathVariable @Parameter(description = "이미지ID", example = "1") Long profileImageId
    ) {
        return fileService.getProfileImageId(domainType,userId,profileImageId);
    }
}
