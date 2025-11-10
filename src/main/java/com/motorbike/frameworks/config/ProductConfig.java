package com.motorbike.frameworks.config;

import com.motorbike.business.adapter.ProductRepositoryAdapter;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.business.usecase.GetProductDetailUseCase;
import com.motorbike.business.usecase.impl.GetProductDetailUseCaseImpl;
import com.motorbike.persistence.gateway.ProductPersistenceGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration for Product module
 * Wires together Business Layer and Persistence Layer using Gateway Pattern
 * 
 * CORRECT Dependency Flow (NO import from Persistence to Business):
 * - Business Layer: ProductRepository interface, ProductRepositoryAdapter (uses Gateway interface)
 * - Persistence Layer: ProductPersistenceGateway interface, ProductJpaGateway implementation
 * - Frameworks: Wires Gateway implementation to Adapter
 * 
 * Result: Persistence does NOT import Business entities!
 */
@Configuration
public class ProductConfig {
    
    /**
     * ProductRepository bean
     * Adapter in Business Layer uses Gateway from Persistence Layer
     * Gateway returns Map, Adapter converts to Domain entities
     */
    @Bean
    public ProductRepository productRepository(ProductPersistenceGateway gateway) {
        return new ProductRepositoryAdapter(gateway);
    }
    
    /**
     * GetProductDetailUseCase bean
     * Interface and implementation both in Business Layer
     */
    @Bean
    public GetProductDetailUseCase getProductDetailUseCase(ProductRepository productRepository) {
        return new GetProductDetailUseCaseImpl(productRepository);
    }
}

