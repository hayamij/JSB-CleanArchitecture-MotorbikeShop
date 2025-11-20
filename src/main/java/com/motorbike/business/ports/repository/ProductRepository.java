package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.SanPham;
import java.util.Optional;

/**
 * Repository interface for SanPham (Product) entity
 * Port in the Use Case layer - implementation will be in Adapter layer
 */
public interface ProductRepository {
    
    /**
     * Find product by ID
     * @param productId the product ID
     * @return Optional containing the product if found, empty otherwise
     */
    Optional<SanPham> findById(Long productId);
    
    /**
     * Save a product
     * @param sanPham the product to save
     * @return the saved product with ID
     */
    SanPham save(SanPham sanPham);
    
    /**
     * Check if product exists by ID
     * @param productId the product ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long productId);
    
    /**
     * Find all products
     * @return List of all products
     */
    java.util.List<SanPham> findAll();
}
