package in.bm.GatewayService.FILTER;

import in.bm.GatewayService.SERVICE.JwtFilter;
import io.jsonwebtoken.JwtException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Set;

@Component
public class AuthenticationGlobalFilter
        implements GlobalFilter, Ordered {

    private final JwtFilter jwtFilter;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/auth/otp/send",
            "/auth/otp/verify",
            "/auth/oauth/google",
            "/auth/admin/login"
    );

    public AuthenticationGlobalFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);

        return Mono.fromCallable(() -> jwtFilter.parseClaims(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(claims -> {
                    if (!jwtFilter.validateToken(claims)) {
                        return unauthorized(exchange);
                    }

                    ServerWebExchange mutated = exchange.mutate()
                            .request(r -> r.headers(h ->
                                    h.set("x-user-id", claims.getSubject())))
                            .build();

                    return chain.filter(mutated);
                })
                .onErrorResume(JwtException.class, e -> unauthorized(exchange));
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
