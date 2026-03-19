package com.mockio.file_service.controller;

import com.mockio.file_service.dto.response.FileUploadResponse;
import com.mockio.file_service.dto.response.UserProfileImageResponse;
import com.mockio.file_service.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file/v1/internal")
public class FileController {

    private final FileService fileService;


    @PostMapping("/files")
    public FileUploadResponse upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "profileImageId", required = false) Long profileImageId,
            @RequestPart(value = "domainType") String domainType,
            @RequestPart(value = "domainId") Long domainId
    ) {

        return fileService.upload(domainType,domainId,file, profileImageId);
    }

    @GetMapping("/{domainType}/{userId}/{profileImageId}")
    public UserProfileImageResponse getProfileImageId(
            @PathVariable String domainType,
            @PathVariable Long userId,
            @PathVariable Long profileImageId
    ) {
        return fileService.getProfileImageId(domainType,userId,profileImageId);
    }
}
