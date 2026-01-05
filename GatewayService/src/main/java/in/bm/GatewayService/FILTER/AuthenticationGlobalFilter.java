package in.bm.GatewayService.FILTER;

import in.bm.GatewayService.SERVICE.JwtFilter;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationGlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    private JwtFilter jwtFilter;

    public AuthenticationGlobalFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (publicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7).trim();

        Claims claims;
        try {
            claims = jwtFilter.parseClaims(token);
            if (!jwtFilter.validateToken(claims)) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(r -> r
                        .headers(h -> h
                                .set("X_USER_ID", claims.getSubject()))).build();

        return chain.filter(mutatedExchange);
    }

    public Boolean publicPath(String path) {
        return path.startsWith("/auth/otp/send") ||
                path.startsWith("/auth/otp/verify") ||
                path.startsWith("/auth/oauth/google") ||
                path.startsWith("/auth/admin/login");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
