package com.mockio.auth_service.dto;

import java.util.List;

public record VerifiedTokenClaims(
        String keycloakUserId,
        String username,
        String email,
        String provider,
        List<String> roles
) {}