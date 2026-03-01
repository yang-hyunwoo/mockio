package com.mockio.gateway.dto.response;

public record SessionValidateResponse(String userId,
                                      String username,
                                      String provider,
                                      java.util.List<String> roles) {}