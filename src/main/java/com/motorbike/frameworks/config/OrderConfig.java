package com.motorbike.frameworks.config;

import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.OrderRepository;
import com.motorbike.business.usecase.CheckoutUseCase;
import com.motorbike.business.usecase.impl.CheckoutUseCaseImpl;
import com.motorbike.persistence.adapter.OrderRepositoryAdapter;
import com.motorbike.persistence.repository.OrderJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Order Configuration - Frameworks Layer
 * Spring configuration for order-related beans
 * Part of Clean Architecture - Frameworks Layer
 */
@Configuration
public class OrderConfig {
    
    @Bean
    public OrderRepository orderRepository(OrderJpaRepository jpaRepository) {
        return new OrderRepositoryAdapter(jpaRepository);
    }
    
    @Bean
    public CheckoutUseCase checkoutUseCase(OrderRepository orderRepository,
                                          CartRepository cartRepository) {
        return new CheckoutUseCaseImpl(orderRepository, cartRepository);
    }
}
