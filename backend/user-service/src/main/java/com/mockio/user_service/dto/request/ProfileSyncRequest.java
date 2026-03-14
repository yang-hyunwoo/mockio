package com.mockio.user_service.dto.request;

public record ProfileSyncRequest(
        String keycloakUserId,
        String provider,
        String name,
        String email,
        String givenName
) {}
