package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.Product;
import java.util.Optional;

/**
 * Repository interface for Product entity
 * Port in the Use Case layer - implementation will be in Adapter layer
 */
public interface ProductRepository {
    
    /**
     * Find product by ID
     * @param productId the product ID
     * @return Optional containing the product if found, empty otherwise
     */
    Optional<Product> findById(Long productId);
    
    /**
     * Save a product
     * @param product the product to save
     * @return the saved product with ID
     */
    Product save(Product product);
    
    /**
     * Check if product exists by ID
     * @param productId the product ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long productId);
}
