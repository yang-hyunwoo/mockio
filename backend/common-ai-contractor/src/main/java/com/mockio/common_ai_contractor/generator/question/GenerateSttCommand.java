package com.mockio.common_ai_contractor.generator.question;

/**
 * STT 변환 DTO
 */

import java.io.InputStream;

public record GenerateSttCommand(

        //file
        InputStream inputStream,

        //file 명
        String filename,

        //file type
        String contentType
) {}
