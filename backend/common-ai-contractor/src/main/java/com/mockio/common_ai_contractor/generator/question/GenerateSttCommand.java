package com.mockio.common_ai_contractor.generator.question;


import java.io.InputStream;

public record GenerateSttCommand(
        InputStream inputStream,
        String filename,
        String contentType
) {}
