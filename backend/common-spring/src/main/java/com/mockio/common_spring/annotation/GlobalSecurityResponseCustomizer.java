package com.mockio.common_spring.annotation;

import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalSecurityResponseCustomizer {

    @Bean
    public OpenApiCustomizer securityResponseCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();

                    if (!responses.containsKey("400")) {
                        responses.addApiResponse("400",
                                new io.swagger.v3.oas.models.responses.ApiResponse()
                                        .$ref("#/components/responses/BadRequest"));
                    }


                    boolean hasSecurity = operation.getSecurity() != null && !operation.getSecurity().isEmpty();

                    if (hasSecurity && !responses.containsKey("401")) {
                        responses.addApiResponse("401",
                                new io.swagger.v3.oas.models.responses.ApiResponse()
                                        .$ref("#/components/responses/Unauthorized"));
                    }
                    if (!responses.containsKey("500")) {
                        responses.addApiResponse("500",
                                new io.swagger.v3.oas.models.responses.ApiResponse()
                                        .$ref("#/components/responses/InternalServerError"));
                    }
                })
        );
    }
}
