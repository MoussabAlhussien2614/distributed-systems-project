package com.example.subscription_service.services;

import com.example.subscription_service.dtos.request.BalanceRequest;
import com.example.subscription_service.models.BalanceUser;
import com.example.subscription_service.repositories.BalanceUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceUserService {

    private final BalanceUserRepository repository;

    public List<BalanceUser> findAll() {
        return repository.findAll();
    }

    public Optional<BalanceUser> findById(Long id) {
        return repository.findById(id);
    }

    public BalanceUser save(BalanceRequest request) {
        BalanceUser balanceUser = BalanceUser.builder()
                .userId(request.getUserId())
                .balance(request.getBalance())
                .build();
        return repository.save(balanceUser);
    }

    public BalanceUser update(Long id, BalanceRequest request) {
        BalanceUser existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BalanceUser not found with id: " + id));
        existing.setUserId(request.getUserId());
        existing.setBalance(request.getBalance());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
