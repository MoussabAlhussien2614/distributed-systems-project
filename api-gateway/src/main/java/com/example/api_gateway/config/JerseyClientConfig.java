package com.example.api_gateway.config;

import com.netflix.discovery.shared.transport.jersey3.Jersey3TransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyClientConfig {
    @Bean
    public Jersey3TransportClientFactories transportClientFactories() {
        return new Jersey3TransportClientFactories();
    }
}
