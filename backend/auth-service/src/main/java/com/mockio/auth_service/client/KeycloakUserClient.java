package com.mockio.auth_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KeycloakUserClient {

    private final RestClient restClient;
    @Value("${keycloak.base-url}") private String baseUrl;
    @Value("${keycloak.realm}") private String realm;

//    @CircuitBreaker(name="keycloakUserDelete" , fallbackMethod = "userDeleteFallback")
//    public void deleteUser(String )

}
