package in.bm.GatewayService.CONFIG;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RedisRateLimiterConfig {



    @Bean
    @Primary
    public RedisRateLimiter authRateLimiter() {
        return new RedisRateLimiter(2, 5);
    }

    @Bean
    public RedisRateLimiter movieRateLimiter() {
        return new RedisRateLimiter(50, 100);
    }

    @Bean
    public RedisRateLimiter theaterRateLimiter() {
        return new RedisRateLimiter(20, 30);
    }

    @Bean
    public RedisRateLimiter showRateLimiter() {
        return new RedisRateLimiter(20, 30);
    }

    @Bean
    public RedisRateLimiter bookingRateLimiter() {
        return new RedisRateLimiter(10, 20);
    }

    @Bean
    public RedisRateLimiter paymentRateLimiter() {
        return new RedisRateLimiter(5, 10);
    }

    @Bean
    public RedisRateLimiter botRateLimiter() {
        return new RedisRateLimiter(20, 40);
    }


}
