package com.mockio.gateway.config;

import com.mockio.gateway.dto.response.SessionValidateResponse;
import com.mockio.gateway.util.InternalJwtIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionAuthFilter implements GlobalFilter, Ordered {

    private final WebClient webClient = WebClient.builder().build();

    private final AntPathMatcher matcher = new AntPathMatcher();

    private final InternalJwtIssuer internalJwtIssuer;

    private static final List<String> PERMIT = List.of(
            "/health",
            "/actuator/health/**",
            "/api/users/v1/public/**",
            "/api/user-interview/v1/public/**",
            "/api/notification/v1/public/**",
            "/api/chat/v1/public/**",
            "/api/ai/v1/public/**",
            "/api/auth/v1/public/**",
            "/api/feedback/v1/public/**",
            "/api/noti/v1/public/**",
            "/api/inquiry/v1/public/**",
            "/api/faq/v1/public/**",
            "/.well-known/jwks.json"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("[GW FILTER HIT] " + path);

        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        if (isPermitted(path)) {
            return chain.filter(exchange);
        }

        HttpCookie cookie = exchange.getRequest().getCookies().getFirst("MOCKIO_SESSION");
        if (cookie == null || cookie.getValue() == null || cookie.getValue().isBlank()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }



        // auth-service validate 호출 (쿠키를 그대로 전달)
        return webClient.get()
                .uri("http://localhost:9080/api/auth/v1/public/session/validate")
                .header(HttpHeaders.COOKIE, "MOCKIO_SESSION=" + cookie.getValue())
                .retrieve()
                .bodyToMono(SessionValidateResponse.class)
                .flatMap(res -> {
                    String internalJwt = internalJwtIssuer.issue(res.userId(),res.keycloakUserId(), res.roles());
                    ServerHttpRequest original = exchange.getRequest();

                    ServerHttpRequest newRequest =
                            new ServerHttpRequestDecorator(original) {
                                @Override
                                public HttpHeaders getHeaders() {
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.putAll(super.getHeaders());
                                    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + internalJwt);
                                    return headers;
                                }
                            };

                    return chain.filter(exchange.mutate().request(newRequest).build());
                })
                .onErrorResume(ex -> {
                    System.out.println("🔥 GW ERROR OCCURRED");
                    ex.printStackTrace();
                    return unauthorized(exchange);
                });
    }

    private boolean isPermitted(String path) {
        return PERMIT.stream().anyMatch(p -> matcher.match(p, path));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
