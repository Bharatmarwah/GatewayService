    package in.bm.GatewayService.CONFIG;

    import org.springframework.cloud.gateway.route.RouteLocator;
    import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class GatewayRouteConfig {

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

            return builder.routes()

                    .route("auth",
                            r -> r.path("/auth/public/**")
                                    .filters(f -> f.stripPrefix(1)
                                            .circuitBreaker(c -> c
                                                    .setName("authCB")
                                                    .setFallbackUri("forward:/fallback/default")))
                                    .uri("lb://AUTHSERVICE")
                    )

                    .route("movies",
                            r -> r.path("/movie/movies/**")
                                    .filters(f -> f.stripPrefix(1)
                                            .retry(2)
                                            .circuitBreaker(c -> c
                                                    .setName("movieCB")
                                                    .setFallbackUri("forward:/fallback/default")))
                                    .uri("lb://MOVIESERVICE")
                    )

                    .route("theaters",
                            r -> r.path("/theater/theaters/**")
                                    .filters(f -> f.stripPrefix(1).retry(2)
                                            .circuitBreaker(c -> c
                                                    .setName("movieCB")
                                                    .setFallbackUri("forward:/fallback/default")))
                                    .uri("lb://MOVIESERVICE")
                    )
                    .route("shows",
                            r -> r.path("/show/shows/**")
                                    .filters(f -> f.stripPrefix(1).retry(2)
                                            .circuitBreaker(c -> c
                                                    .setName("movieCB")
                                                    .setFallbackUri("forward:/fallback/default")))
                                    .uri("lb://MOVIESERVICE"))

                    .route("booking",
                            r->r.path("/booking/bookings/**")
                                    .filters(f->f.stripPrefix(1)
                                            .circuitBreaker(c->c.setName("bookCb").setFallbackUri("forward:/fallback/default")))
                                    .uri("lb://BOOKINGSERVICE"))

                    .build();
        }
    }

