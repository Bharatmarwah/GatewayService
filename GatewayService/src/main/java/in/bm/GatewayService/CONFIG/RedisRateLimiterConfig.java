package in.bm.GatewayService.CONFIG;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RedisRateLimiterConfig {

    @Bean
    public RedisRateLimiter authRateLimiter() {
        return new RedisRateLimiter(5, 5);
    }

    @Bean
    public RedisRateLimiter movieRateLimiter() {
        return new RedisRateLimiter(50, 100);
    }

    @Bean
    public RedisRateLimiter theaterRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public RedisRateLimiter showRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public RedisRateLimiter bookingRateLimiter() {
        return new RedisRateLimiter(5, 5);
    }
    @Bean
    public KeyResolver userKeyResolver(){
        return exchange ->
                Mono.justOrEmpty
                        (exchange.getRequest()
                                .getHeaders()
                                .getFirst("X-User-Id"))
                        .defaultIfEmpty("anonymous");
    }

}
