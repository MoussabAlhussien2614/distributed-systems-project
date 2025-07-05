package com.example.user_service.services;

import com.example.user_service.config.JWTService;
import com.example.user_service.dtos.BalanceUser;
import com.example.user_service.dtos.request.AuthenticationRequest;
import com.example.user_service.dtos.request.ChangePasswordRequest;
import com.example.user_service.dtos.request.RegisterRequest;
import com.example.user_service.dtos.request.UpdateUserRequest;
import com.example.user_service.dtos.response.AuthenticationResponse;
import com.example.user_service.dtos.response.UserResponse;
import com.example.user_service.exceptionHandler.LoginUnauthorizedException;
import com.example.user_service.models.Role;
import com.example.user_service.models.User;
import com.example.user_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
  //  private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    private final DiscoveryClient  discoveryClient;
    
    public AuthenticationResponse registerInstructor(RegisterRequest request, User currentUser) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }


        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.INSTRUCTOR)
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .role(user.getRole())
                .build();
    }


    public AuthenticationResponse registerUser(RegisterRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }


        var user = User.builder()
        .firstName(request.getFirstname())
        .lastName(request.getLastname())
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

        repository.save(user);
        System.out.println("hi ther");
        String url = discoveryClient.getInstances("subscription-service")
            .get(0)
            .getUri()
            .toString() + "/api/balances";
        
        System.out.println(url);
        // String url = "http//localhost:8083/api/balances";
        RestTemplate restTemplate = new RestTemplateBuilder()
         .build();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", user.getId());
        requestBody.put("balance", (long) 1000000000);
        
        ResponseEntity<BalanceUser> userBalance = restTemplate
            .postForEntity(url, new HttpEntity<>(requestBody), BalanceUser.class);
        
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .role(user.getRole())
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new LoginUnauthorizedException("User not found with email: " + request.getUsername()));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new LoginUnauthorizedException("Incorrect password");
        }

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .role(user.getRole())
                .build();
    }



//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userUsername;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userUsername = jwtService.extractUsername(refreshToken);
//        if (userUsername != null) {
//            var user = this.repository.findByUsername(userUsername)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }

    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public UserResponse getUserById(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    public UserResponse updateUser(Integer id, UpdateUserRequest request, User currentUser) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));



        // Update fields
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());


        repository.save(user);
        return mapToUserResponse(user);
    }

    public void deleteUser(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repository.deleteById(id);
    }

    public void changePassword(Integer id, ChangePasswordRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
