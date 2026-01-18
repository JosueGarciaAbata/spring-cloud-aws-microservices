package com.josue.microservice_item.configs;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> circuitBreakerCustomizer() {
        return (factory) -> factory.configureDefault(id -> {
                return new Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(CircuitBreakerConfig.custom()
                                .slidingWindowSize(10)
                                .failureRateThreshold(50) // limite para aceptar errores (50%)
                                .waitDurationInOpenState(Duration.ofSeconds(10L))
                                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(10L))
                                .permittedNumberOfCallsInHalfOpenState(5) // De igual forma, 50% como limite para errores.
                                .slowCallDurationThreshold(Duration.ofSeconds(2L))
                                .slowCallRateThreshold(50)
                                .build()
                        )
                        .timeLimiterConfig(TimeLimiterConfig.custom()
                                // EL timeout SIEMPRE debe ser MAYOR que el slow call threshold.
                                .timeoutDuration(Duration.ofSeconds(5L))
                                .build())
                        .build();
            }
        );
    }


}
