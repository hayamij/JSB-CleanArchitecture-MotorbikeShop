package com.motorbike.persistence.repository;

import com.motorbike.persistence.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Order JPA Repository - Persistence Layer
 * Spring Data JPA repository for Order persistence
 * Part of Clean Architecture - Persistence Layer (framework-dependent)
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    
    /**
     * Find all orders by user ID
     * @param userId user ID
     * @return list of orders
     */
    List<OrderJpaEntity> findByUserId(Long userId);
}
