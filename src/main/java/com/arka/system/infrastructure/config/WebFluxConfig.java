package com.arka.system.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * Configuración para WebFlux - Programación Reactiva
 */
@Configuration
@EnableWebFlux
@Slf4j
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/reactive/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    /**
     * Router functions para endpoints reactivos funcionales
     */
    @Bean
    public RouterFunction<ServerResponse> reactiveRoutes() {
        return route()
                .GET("/api/reactive/health", request -> 
                    ServerResponse.ok()
                        .bodyValue("Reactive API is running"))
                .GET("/api/reactive/info", request ->
                    ServerResponse.ok()
                        .bodyValue(java.util.Map.of(
                            "service", "Arka System",
                            "version", "1.0.0",
                            "reactive", true,
                            "timestamp", System.currentTimeMillis()
                        )))
                .build();
    }
}
