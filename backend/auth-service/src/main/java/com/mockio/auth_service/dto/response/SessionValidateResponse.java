package com.mockio.auth_service.dto.response;

public record SessionValidateResponse(Long userId,
                                      String keycloakUserId,
                                      String username,
                                      String email,
                                      String provider,
                                      java.util.List<String> roles) {}
