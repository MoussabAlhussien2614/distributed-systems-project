package com.example.subscription_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.subscription_service.dtos.request.SubscriptionRequest;
import com.example.subscription_service.dtos.response.SubscriptionResponse;
import com.example.subscription_service.models.Subscription;
import com.example.subscription_service.repositories.SubscriptionRepository;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;


    public SubscriptionService(SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository = subscriptionRepository; 
    }

    public List<SubscriptionResponse> list(){
        List<SubscriptionResponse> subs = subscriptionRepository.findAll().stream()
            .map((Subscription sub) -> SubscriptionResponse.builder()
                .id(sub.getId())
                .studentId(sub.getStudentId())
                .courseId(sub.getCourseId())
                .status(sub.getStatus())
                .build())
            .collect(Collectors.toList());
        
        return subs;
    }


      public SubscriptionResponse create(SubscriptionRequest request){
        Subscription sub =  Subscription.builder()
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .status(request.getStatus())
                .build();
        subscriptionRepository.save(sub);
        return SubscriptionResponse.builder()
                .id(sub.getId())
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .status(request.getStatus())
                .build();
    }
}
