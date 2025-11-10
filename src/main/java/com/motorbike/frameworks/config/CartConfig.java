package com.motorbike.frameworks.config;

import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.business.usecase.AddToCartUseCase;
import com.motorbike.business.usecase.impl.AddToCartUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cart Configuration - Frameworks Layer
 * Spring configuration for cart-related beans
 * Part of Clean Architecture - Frameworks Layer
 */
@Configuration
public class CartConfig {
    
    @Bean
    public AddToCartUseCase addToCartUseCase(CartRepository cartRepository, 
                                             ProductRepository productRepository) {
        return new AddToCartUseCaseImpl(cartRepository, productRepository);
    }
}
