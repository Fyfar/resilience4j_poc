package com.kapacik.resilience4j.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import com.kapacik.resilience4j.exception.BusinessException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.monitoring.health.CircuitBreakerHealthIndicator;
import io.vavr.API;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JCircuitBreakerConfig {

    private static final String CIRCUIT_BREAKER_NAME = "endpoint";

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry,
            CircuitBreakerProperties circuitBreakerProperties) {
        CircuitBreakerConfig circuitBreakerConfig = circuitBreakerProperties.buildCircuitBreakerConfig(
                circuitBreakerProperties.getBackends().get("endpointClient"))
                .waitDurationInOpenState(Duration.ofMillis(5_000))
                .recordFailure(throwable -> Match(throwable).of(
                        API.Case(API.$(instanceOf(BusinessException.class)), false),
                        Case($(), true)))
                .build();

        return circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME, circuitBreakerConfig);
    }

    @Bean
    public HealthIndicator endpoints(CircuitBreakerRegistry circuitBreakerRegistry){
        return new CircuitBreakerHealthIndicator(circuitBreakerRegistry.circuitBreaker("endpoint"));
    }

}
