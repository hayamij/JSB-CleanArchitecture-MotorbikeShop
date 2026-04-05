package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.SanPham;
import java.util.List;
import java.util.Optional;

public interface SanPhamRepository {
    
    /**
     * Find product by ID
     */
    Optional<SanPham> findById(Long id);
    
    /**
     * Save product
     */
    SanPham save(SanPham sanPham);
    
    /**
     * Find all products
     */
    List<SanPham> findAll();
    
    /**
     * Delete product by ID
     */
    void deleteById(Long id);
    
    /**
     * Check if product exists by ID
     */
    boolean existsById(Long id);
    
    /**
     * Find products by category
     */
    List<SanPham> findByLoai(String loai);
    
    /**
     * Search products by keyword
     */
    List<SanPham> searchByKeyword(String keyword);
}
