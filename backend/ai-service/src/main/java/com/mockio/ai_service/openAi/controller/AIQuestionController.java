package com.mockio.ai_service.openAi.controller;

import com.mockio.common_ai_contractor.generator.GenerateQuestionCommand;
import com.mockio.common_ai_contractor.generator.GeneratedQuestion;
import com.mockio.common_ai_contractor.generator.InterviewQuestionGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/questions")
@RequiredArgsConstructor
public class AIQuestionController {

    private final InterviewQuestionGenerator questionGenerator;

    @PostMapping("/generate")
    public List<GeneratedQuestion> generate(@RequestBody GenerateQuestionCommand command) {
        return questionGenerator.generate(command);
    }
}
