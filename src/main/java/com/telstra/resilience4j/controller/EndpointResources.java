package com.telstra.resilience4j.controller;

import com.telstra.resilience4j.service.BusinessService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/endpoint")
public class EndpointResources {

    private final BusinessService endpointService;

    public EndpointResources(@Qualifier("endpointService") BusinessService endpointService){
        this.endpointService = endpointService;
    }

    @GetMapping("failure")
    public String backendBFailure(){
        return endpointService.failure();
    }

    @GetMapping("success")
    public String backendBSuccess(){
        return endpointService.success();
    }

    @GetMapping("ignore")
    public String ignore(){
        return endpointService.ignore();
    }
}
