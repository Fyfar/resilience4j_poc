package com.kapacik.resilience4j.connnector;

import io.reactivex.Observable;

public interface RemoteClient {
    String failure();

    String success();

    String ignoreException();

    Observable<String> methodWhichReturnsAStream();
}
