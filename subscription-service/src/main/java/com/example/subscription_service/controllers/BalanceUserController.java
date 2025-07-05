package com.example.subscription_service.controllers;

import com.example.subscription_service.dtos.request.BalanceRequest;
import com.example.subscription_service.models.BalanceUser;
import com.example.subscription_service.services.BalanceUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceUserController {

    private final BalanceUserService service;

    @GetMapping
    public ResponseEntity<List<BalanceUser>> getAllBalances() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BalanceUser> getBalanceById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BalanceUser> createBalance(@RequestBody BalanceRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BalanceUser> updateBalance(@PathVariable Long id, @RequestBody BalanceRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBalance(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
