package com.example.subscription_service.services;

import java.util.List;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.subscription_service.dtos.CourseFetched;
import com.example.subscription_service.dtos.request.SubscriptionRequest;
import com.example.subscription_service.dtos.response.SubscriptionResponse;
import com.example.subscription_service.models.BalanceUser;
import com.example.subscription_service.models.Subscription;
import com.example.subscription_service.repositories.BalanceUserRepository;
import com.example.subscription_service.repositories.SubscriptionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final BalanceUserRepository balanceUserRepository;
    private final DiscoveryClient discoveryClient;


    public SubscriptionService(SubscriptionRepository subscriptionRepository,BalanceUserRepository balanceUserRepository, DiscoveryClient discoveryClient){
        this.balanceUserRepository = balanceUserRepository;
        this.subscriptionRepository = subscriptionRepository; 
        this.discoveryClient = discoveryClient;
    }

    public List<SubscriptionResponse> list() {
        return subscriptionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList(); // Java 16+ alternative to Collectors.toList()
    }


      public SubscriptionResponse create(SubscriptionRequest request){

        Subscription sub =  Subscription.builder()
          .studentId(request.getStudentId())
          .courseId(request.getCourseId())
          .build();
        BalanceUser balanceUser = balanceUserRepository.findById(request.getStudentId())
            .orElseThrow(()-> new EntityNotFoundException("user balance not found"));          
        
        String url = discoveryClient.getInstances("course-service")
            .get(0)
            .getUri()
            .toString() + "/api/courses/{id}";
      
        RestTemplate restTemplate = new RestTemplate();
        CourseFetched course = restTemplate.getForObject(url, CourseFetched.class, request.getCourseId());
   
        balanceUser.setBalance(balanceUser.getBalance() -  course.getTutionFee().longValue());
        balanceUserRepository.save(balanceUser);
        System.out.println("new balance");
        System.out.println(balanceUser.getBalance());
        subscriptionRepository.save(sub);
        
        return SubscriptionResponse.builder()
                .id(sub.getId())
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .build();
    }


    private SubscriptionResponse convertToResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .studentId(subscription.getStudentId())
                .courseId(subscription.getCourseId())
                .build();
    }
}
