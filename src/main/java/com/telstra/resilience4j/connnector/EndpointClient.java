package com.telstra.resilience4j.connnector;


import com.telstra.resilience4j.exception.BusinessException;

import io.reactivex.Observable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

@Component(value = "endpointClient")
public class EndpointClient implements RemoteClient {

    @Override
    public String failure() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
    }

    @Override
    public String success() {
        return "Hello World from endpoint client";
    }

    @Override
    public String ignoreException() {
        throw new BusinessException("This exception is ignored by the CircuitBreaker of endpoint client");
    }

    @Override
    public Observable<String> methodWhichReturnsAStream() {
        return Observable.never();
    }
}
