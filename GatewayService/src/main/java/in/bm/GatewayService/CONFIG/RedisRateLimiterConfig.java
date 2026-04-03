package in.bm.GatewayService.CONFIG;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@Configuration
public class RedisRateLimiterConfig {

    @Bean
    @Primary
    public RedisRateLimiter defaultRateLimiter() {
        return new RedisRateLimiter(10, 20);
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
