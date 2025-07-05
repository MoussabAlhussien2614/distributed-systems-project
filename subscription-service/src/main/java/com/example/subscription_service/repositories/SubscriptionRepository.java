package com.example.subscription_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.subscription_service.models.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription ,Long>{

    
    public List<Subscription> findByStudentId(Long id);
}
