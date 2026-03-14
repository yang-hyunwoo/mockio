package com.mockio.auth_service.dto.request;

public record ProfileSyncRequest(
        String keycloakUserId,
        String provider,
        String name,
        String email
) {}
