package com.telstra.resilience4j.service;


import com.telstra.resilience4j.connnector.RemoteClient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service(value = "endpointService")
public class EndpointServiceImpl implements BusinessService  {

    private final RemoteClient endpointClient;
    private final CircuitBreaker circuitBreaker;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public EndpointServiceImpl(@Qualifier("endpointClient") RemoteClient endpointClient,
            CircuitBreaker circuitBreaker) {
        this.endpointClient = endpointClient;
        this.circuitBreaker = circuitBreaker;

        scheduler.scheduleAtFixedRate(() -> {
                    //simulate service is broken
                    circuitBreaker.transitionToOpenState();
                    System.out.println("Health-check is negative, set circuitBreaker to OPEN state");
                },
                1, 10, TimeUnit.SECONDS);
    }

    @Override
    public String failure() {
        return CircuitBreaker.decorateSupplier(circuitBreaker, endpointClient::failure).get();
    }

    @Override
    public String success() {
        return Try.ofSupplier(CircuitBreaker.decorateSupplier(circuitBreaker, endpointClient::success))
                .recover(this::recovery)
                .get();
    }

    @Override
    public String ignore() {
        return CircuitBreaker.decorateSupplier(circuitBreaker, endpointClient::ignoreException).get();
    }

    @Override
    public Try<String> methodWithRecovery() {
        Supplier<String> backendFunction = CircuitBreaker.decorateSupplier(circuitBreaker, endpointClient::failure);
        return Try.ofSupplier(backendFunction).recover(this::recovery);
    }

    private String recovery(Throwable throwable) {
        // Handle exception and invoke fallback
        return "Endpoint client is down, will be up soon";
    }
}
