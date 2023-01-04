package com.beyonder.gatewayservice.config;

import com.beyonder.gatewayservice.filter.AdminFilter;
import com.beyonder.gatewayservice.filter.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfiguration {
    private final UserFilter filter;
    private final AdminFilter adminFilter;

    @Value("${port.auth}")
    private String PORT_AUTH;
    @Value("${port.consumer}")
    private String PORT_CONSUMER;
    @Value("${port.publisher}")
    private String PORT_PUBLISHER;
    @Value("${port.lesson}")
    private String PORT_LESSON;

    private final String endPoint8080 = "http://localhost:8080/";
    @Bean
    RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/lessons/**")
                        .and().method("POST", "PUT", "DELETE")
                        .filters(f -> f.filter(filter).filter(adminFilter))
                        .uri(PORT_LESSON))
                .route(r -> r.path("/api/v1/lessons/**")
                        .and().method("GET")
                        .filters(f -> f.filter(filter))
                        .uri(PORT_LESSON))
                .route(r -> r.path("/api/v1/userLessons/**")
                        .and().method("GET")
                        .filters(f -> f.filter(filter))
                        .uri(PORT_LESSON))
                .route(r -> r.path("/api/v1/jobs/**")
                        .and().method("POST", "PUT", "DELETE")
                        .filters(f -> f.filter(filter).filter(adminFilter))
                        .uri("http://localhost:8082/"))
                .route(r -> r.path("/api/v1/jobs/**")
                        .and().method("GET")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8082/"))
                .route(r -> r.path("/api/v1/userJobs/**")
                        .and().method("GET")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8082/"))
                .route(r -> r.path("/api/v1/lesson_reviews/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8083/"))
                .route(r -> r.path("/api/v1/publisher/**")
                        .filters(f -> f.filter(filter))
                        .uri(PORT_PUBLISHER))
                .route(r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8084/"))
                .route(r -> r.path("/api/v1/userCerts/**")
                        .and().method("GET")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8084/"))
                .route(r -> r.path("/api/v1/auth/all-users")
                        .filters(f -> f.filter(filter).filter(adminFilter))
                        .uri(PORT_AUTH))
                .route(r -> r.path("/api/v1/auth/**")
                        .uri(PORT_AUTH))

                .build();
    }
}
