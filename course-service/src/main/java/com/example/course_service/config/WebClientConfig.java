package com.example.course_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

@Configuration
@LoadBalancerClient(name = "user-service", configuration = LoadBalancerConfiguration.class)
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Configuration
class LoadBalancerConfiguration {
    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder()
                .withBlockingDiscoveryClient()
                .build(context);
    }

    @Bean
    public ReactorServiceInstanceLoadBalancer customLoadBalancer(ServiceInstanceListSupplier supplier) {
        return new CustomLoadBalancer(supplier);
    }
}
