package uz.katm.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Set;

/**
 * Проверяет соответствие IP-адреса клиента организации (head/code) через katm-auth.
 * head и code берутся из JWT-claims (Keycloak user attributes).
 * Запускается после Spring Security (@Order(1) > -100).
 */
@Slf4j
@Order(1)
@Component
public class AccessCheckFilter implements WebFilter {

    private static final Set<String> PUBLIC_PREFIXES = Set.of(
            "/api/v1/auth",
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars",
            "/favicon.ico"
    );

    private final WebClient webClient;
    private final boolean accessCheckEnabled;

    public AccessCheckFilter(
            @Value("${katm.auth-service.url:http://localhost:8081}") String authServiceUrl,
            @Value("${katm.access-check.enabled:true}") boolean accessCheckEnabled
    ) {
        this.webClient = WebClient.create(authServiceUrl);
        this.accessCheckEnabled = accessCheckEnabled;
    }

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (!accessCheckEnabled || isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // defaultIfEmpty гарантирует эмит одного элемента — switchIfEmpty на Mono<Void> не работает
        return ReactiveSecurityContextHolder.getContext()
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optCtx -> {
                    if (optCtx.isEmpty()) {
                        return chain.filter(exchange);
                    }
                    Authentication auth = optCtx.get().getAuthentication();
                    if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
                        return chain.filter(exchange);
                    }
                    String head = jwtAuth.getToken().getClaimAsString("head");
                    String code = jwtAuth.getToken().getClaimAsString("code");

                    if (head == null || head.isBlank() || code == null || code.isBlank()) {
                        log.warn("JWT missing head/code claims: path={}, sub={}",
                                path, jwtAuth.getToken().getSubject());
                        return reject(exchange, HttpStatus.FORBIDDEN,
                                "Access denied: missing organization attributes in token");
                    }

                    String ip = extractClientIp(exchange);
                    log.debug("Access check: path={}, head={}, code={}, ip={}", path, head, code, ip);
                    return performAccessCheck(exchange, chain, head, code, ip);
                });
    }

    private Mono<Void> performAccessCheck(ServerWebExchange exchange, WebFilterChain chain,
                                          String head, String code, String ip) {
        return webClient.post()
                .uri("/internal/access-check")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AccessCheckRequest(head, code, ip))
                .retrieve()
                .bodyToMono(AccessCheckResponse.class)
                .flatMap(response -> {
                    if (!response.allowed()) {
                        log.warn("Access denied: head={}, code={}, ip={}, reason={}",
                                head, code, ip, response.message());
                        return reject(exchange, HttpStatus.FORBIDDEN, response.message());
                    }
                    log.debug("Access allowed: head={}, code={}, ip={}", head, code, ip);
                    return chain.filter(exchange);
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("Access check returned {}: head={}, code={}, ip={}",
                            ex.getStatusCode(), head, code, ip);
                    return reject(exchange, HttpStatus.FORBIDDEN, "Access denied");
                })
                .onErrorResume(ex -> {
                    log.error("Access check service unavailable: {}", ex.getMessage());
                    return reject(exchange, HttpStatus.SERVICE_UNAVAILABLE, "Auth service unavailable");
                });
    }

    private Mono<Void> reject(ServerWebExchange exchange, HttpStatus status, String message) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.empty();
        }
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] body = ("{\"error\":\"" + message + "\"}").getBytes();
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(body)));
    }

    private String extractClientIp(ServerWebExchange exchange) {
        String xff = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String realIp = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        InetSocketAddress remote = exchange.getRequest().getRemoteAddress();
        return remote != null ? remote.getAddress().getHostAddress() : "unknown";
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
    }

    private record AccessCheckRequest(String head, String code, String ip) {}

    private record AccessCheckResponse(boolean allowed, String message) {}
}
