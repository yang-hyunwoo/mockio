package com.mockio.core_service.ai.controller;

import com.mockio.common_ai_contractor.generator.question.GenerateSttCommand;
import com.mockio.common_ai_contractor.generator.question.GeneratedStt;
import com.mockio.common_ai_contractor.generator.question.SttGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/ai/v1/answer")
@RequiredArgsConstructor
public class AIAnswerController {

    private final SttGenerator sttGenerator;

    @PostMapping(value = "/stt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GeneratedStt generateStt(@RequestParam("file") MultipartFile file) throws IOException {
        GenerateSttCommand command = new GenerateSttCommand(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );
       return sttGenerator.generate(command);
    }

}
