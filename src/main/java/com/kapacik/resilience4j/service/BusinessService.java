package com.kapacik.resilience4j.service;


public interface BusinessService {
    String failure();

    String success();

    String ignore();
}
