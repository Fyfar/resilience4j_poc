package com.kapacik.resilience4j.service;


import com.kapacik.resilience4j.connnector.RemoteClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service(value = "endpointService")
public class EndpointServiceImpl implements BusinessService  {

    private final RemoteClient endpointClient;
    private final CircuitBreaker circuitBreaker;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public EndpointServiceImpl(@Qualifier("endpointClient") RemoteClient endpointClient,
            CircuitBreaker circuitBreaker) {
        this.endpointClient = endpointClient;
        this.circuitBreaker = circuitBreaker;

        //uncomment to enable backend switch between OPEN/CLOSE circuit states
        /*scheduler.scheduleAtFixedRate(() -> {
                    //simulate service is broken
                    circuitBreaker.transitionToOpenState();
                    System.out.println("Health-check is negative, set circuitBreaker to OPEN state");
                },
                1, 10, TimeUnit.SECONDS);*/
    }

    @Override
    public String failure() {
        return endpointClient.failure();
    }

    @Override
    public String success() {
        return Try.of(endpointClient::success).recover(this::recovery).get();
    }

    @Override
    public String ignore() {
        return endpointClient.ignoreException();
    }

    private String recovery(Throwable throwable) {
        // Handle exception and invoke fallback
        return "Endpoint client is down, will be up soon";
    }
}
