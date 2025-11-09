package com.motorbike.domain.repository;

import com.motorbike.domain.entity.Product;
import java.util.List;
import java.util.Optional;

/**
 * Domain Repository Interface: ProductRepository
 * Defines the contract for Product persistence operations
 * This is part of the domain layer and is framework-agnostic
 * Implementation will be in the infrastructure layer
 */
public interface ProductRepository {
    
    /**
     * Find a product by its ID
     * @param id Product ID
     * @return Optional containing the product if found
     */
    Optional<Product> findById(Long id);
    
    /**
     * Find all products
     * @return List of all products
     */
    List<Product> findAll();
    
    /**
     * Find products by category
     * @param category Product category
     * @return List of products in the category
     */
    List<Product> findByCategory(String category);
    
    /**
     * Find available products only
     * @return List of available products
     */
    List<Product> findAvailableProducts();
    
    /**
     * Save a product (create or update)
     * @param product Product to save
     * @return Saved product
     */
    Product save(Product product);
    
    /**
     * Delete a product by ID
     * @param id Product ID
     */
    void deleteById(Long id);
    
    /**
     * Check if a product exists
     * @param id Product ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);
}
