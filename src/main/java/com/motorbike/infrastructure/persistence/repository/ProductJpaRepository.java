package com.motorbike.infrastructure.persistence.repository;

import com.motorbike.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for Product
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    
    /**
     * Find products by category
     */
    List<ProductJpaEntity> findByCategory(String category);
    
    /**
     * Find available products
     */
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.available = true")
    List<ProductJpaEntity> findAvailableProducts();
    
    /**
     * Find products in stock
     */
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.available = true AND p.stockQuantity > 0")
    List<ProductJpaEntity> findInStockProducts();
}
