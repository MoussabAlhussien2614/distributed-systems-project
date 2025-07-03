package com.example.subscription_service.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.subscription_service.dtos.request.SubscriptionRequest;
import com.example.subscription_service.dtos.response.SubscriptionResponse;
import com.example.subscription_service.services.SubscriptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


    public SubscriptionController(SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;;
    }

    @GetMapping("/status")
    public String status(){
        return "status";
    }

    @GetMapping()
    public List<SubscriptionResponse> list(){
        var response = subscriptionService.list();
        return response;
    }

    
    @PostMapping()
    public SubscriptionResponse create( @RequestBody SubscriptionRequest request){
        var response = subscriptionService.create(request);
        return response;
    }

}
