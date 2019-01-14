package com.kapacik.resilience4j.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.monitoring.health.CircuitBreakerHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Resilience4JCircuitBreakerConfig {

    private static final String CIRCUIT_BREAKER_NAME = "endpointClient";

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry,
            CircuitBreakerProperties circuitBreakerProperties) {
        return circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME);
    }

    @Bean
    public HealthIndicator endpoints(CircuitBreakerRegistry circuitBreakerRegistry){
        return new CircuitBreakerHealthIndicator(circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME));
    }

}
