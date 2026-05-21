package uz.katm.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.Instant;

@Slf4j
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = Instant.now().toEpochMilli();
        log.info("Gateway request: {} {}", request.getMethod(), request.getURI().getPath());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = Instant.now().toEpochMilli() - startTime;
            log.info("Gateway response: {} {} [{}ms]",
                exchange.getResponse().getStatusCode(), request.getURI().getPath(), duration);
        }));
    }

    @Override
    public int getOrder() { return -1; }
}
