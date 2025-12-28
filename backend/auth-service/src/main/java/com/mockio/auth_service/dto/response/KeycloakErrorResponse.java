package com.mockio.auth_service.dto.response;

public record KeycloakErrorResponse(
        String error,
        String error_description
) {}
