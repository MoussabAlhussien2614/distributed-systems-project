package com.example.user_service.controllers;

import com.example.user_service.dtos.request.AuthenticationRequest;
import com.example.user_service.dtos.request.RegisterRequest;
import com.example.user_service.dtos.response.AuthenticationResponse;
import com.example.user_service.models.User;
import com.example.user_service.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    //  Register INSTRUCTOR Only By ADMIN
    @PostMapping("/register/instructor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthenticationResponse> registerInstructor(
         @Valid @RequestBody RegisterRequest request,
         @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(service.registerInstructor(request, currentUser));
    }

    //  Register USER Only By ADMIN
    @PostMapping("/register/user")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.registerUser(request));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid   @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/status")
    public String status (){
        return " Hello World! ";
    }

}
