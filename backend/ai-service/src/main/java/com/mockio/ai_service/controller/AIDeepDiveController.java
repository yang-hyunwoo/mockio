package com.mockio.ai_service.controller;



import com.mockio.common_ai_contractor.generator.deepdive.DeepDiveGenerator;
import com.mockio.common_ai_contractor.generator.deepdive.GenerateDeepDiveCommand;
import com.mockio.common_ai_contractor.generator.deepdive.GeneratedDeepDiveBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/ai/v1/questions")
@RequiredArgsConstructor
public class AIDeepDiveController {

    private final DeepDiveGenerator deepDiveGenerator;

    @PostMapping("/deepdive/validate-and-generate")
    public GeneratedDeepDiveBundle generate(@RequestBody GenerateDeepDiveCommand command) {
        return deepDiveGenerator.generate(command);
    }

}
