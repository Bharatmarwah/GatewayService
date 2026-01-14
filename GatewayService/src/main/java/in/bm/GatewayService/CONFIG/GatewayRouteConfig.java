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
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://AUTHSERVICE")
                )


                .route("movies",
                        r -> r.path("/movie/movies/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://MOVIESERVICE")
                )

                .route("theaters",
                        r -> r.path("/theater/theaters/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://MOVIESERVICE")
                )
                .route("shows",
                        r->r.path("/show/shows/**")
                                .filters(f->f.stripPrefix(1))
                                .uri("lb://MOVIESERVICE"))

                .build();
    }
}

