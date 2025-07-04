package com.example.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    private static final List<String> WHITELISTED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/eureka"
    );

    private static final List<List<String>> ADMIN_PATHS = List.of(
        List.of("PUT",".*/api/courses/\\d+/approval$")
    );

    
    private static final List<List<String>> INSTRUCTOR_PATHS = List.of(
        List.of("PUT",".*/api/courses/\\d+$")
                
    );

    
    private static final List<List<String>> USER_PATHS = List.of(

    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();

        // Skip JWT validation for whitelisted paths
        if (WHITELISTED_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String jwt = authHeader.substring(7);

        if (!jwtService.isTokenValid(jwt)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        String userId = jwtService.extractUserId(jwt);
        String username = jwtService.extractUsername(jwt);
        String roles = jwtService.extractRoles(jwt);
        System.out.println(roles.contains("ADMIN"));
        if (ADMIN_PATHS.stream()
            .anyMatch((list) -> list.get(0).equals(request.getMethod().toString()) && path.matches(list.get(1)))
            &&  !roles.contains("ADMIN")){

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        if (INSTRUCTOR_PATHS.stream()
            .anyMatch((list) -> list.get(0).equals(request.getMethod().toString()) && path.matches(list.get(1)))
            &&  !roles.contains("INSTRUCTOR")){

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        if (USER_PATHS.stream()
            .anyMatch((list) -> list.get(0).equals(request.getMethod().toString()) && path.matches(list.get(1)))
            &&  !roles.contains("USER")){

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        // Extract user info from token
        
        // Mutate request with user info headers
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-User-Id", userId)
                .header("X-User-Name", username)
                .header("X-User-Roles", roles)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
