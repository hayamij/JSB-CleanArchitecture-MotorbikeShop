package com.motorbike.persistence.gateway;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Gateway interface for Persistence Layer
 * Returns raw data (Map) instead of domain entities
 * This way Persistence Layer does NOT depend on Business Layer
 * 
 * Business Layer will define ProductRepositoryAdapter that uses this gateway
 * and converts Map to Domain entities
 */
public interface ProductPersistenceGateway {
    
    /**
     * Find product by ID
     * @param id Product ID
     * @return Optional Map containing product data (field name -> value)
     */
    Optional<Map<String, Object>> findById(Long id);
    
    /**
     * Find all products
     * @return List of Maps containing product data
     */
    List<Map<String, Object>> findAll();
    
    /**
     * Find products by category
     * @param category Category name
     * @return List of Maps containing product data
     */
    List<Map<String, Object>> findByCategory(String category);
    
    /**
     * Find available products
     * @return List of Maps containing product data
     */
    List<Map<String, Object>> findAvailableProducts();
    
    /**
     * Save product
     * @param productData Map containing product data to save
     * @return Map containing saved product data
     */
    Map<String, Object> save(Map<String, Object> productData);
    
    /**
     * Delete product by ID
     * @param id Product ID
     */
    void deleteById(Long id);
    
    /**
     * Check if product exists
     * @param id Product ID
     * @return true if exists
     */
    boolean existsById(Long id);
}
