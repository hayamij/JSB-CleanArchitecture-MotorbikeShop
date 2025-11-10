package com.motorbike.persistence.repository;

import com.motorbike.persistence.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Cart JPA Repository - Persistence Layer
 * Spring Data JPA repository for cart database operations
 * Part of Clean Architecture - Persistence Layer (framework-dependent)
 */
@Repository
public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {
    
    /**
     * Find cart by user ID
     * @param userId user ID
     * @return Optional containing cart if found
     */
    Optional<CartJpaEntity> findByUserId(Long userId);
    
    /**
     * Check if cart exists for user
     * @param userId user ID
     * @return true if cart exists
     */
    boolean existsByUserId(Long userId);
    
    /**
     * Delete cart by user ID
     * @param userId user ID
     */
    void deleteByUserId(Long userId);
}
