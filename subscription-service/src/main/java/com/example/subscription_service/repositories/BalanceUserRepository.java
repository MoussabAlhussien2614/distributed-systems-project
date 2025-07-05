package com.example.subscription_service.repositories;

import com.example.subscription_service.models.BalanceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceUserRepository extends JpaRepository<BalanceUser, Long> {

}
