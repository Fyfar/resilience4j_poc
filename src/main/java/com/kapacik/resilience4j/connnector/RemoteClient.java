package com.kapacik.resilience4j.connnector;

public interface RemoteClient {
    String failure();

    String success();

    String ignoreException();
}
