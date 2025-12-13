package com.mockio.auth_service.dto;

public record KeycloakErrorResponse(
        String error,
        String error_description
) {}
