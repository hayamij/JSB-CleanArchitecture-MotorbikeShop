package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.DonHangJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for DonHangJpaEntity
 * Provides CRUD operations and custom queries for orders
 */
@Repository
public interface DonHangJpaRepository extends JpaRepository<DonHangJpaEntity, Long> {
    
    /**
     * Find all orders by user ID
     * @param maTaiKhoan User account ID
     * @return List of orders
     */
    List<DonHangJpaEntity> findByMaTaiKhoan(Long maTaiKhoan);
    
    /**
     * Find orders by status
     * @param trangThai Order status
     * @return List of orders with given status
     */
    List<DonHangJpaEntity> findByTrangThai(String trangThai);
    
    /**
     * Find orders by user ID and status
     * @param maTaiKhoan User account ID
     * @param trangThai Order status
     * @return List of orders matching criteria
     */
    List<DonHangJpaEntity> findByMaTaiKhoanAndTrangThai(Long maTaiKhoan, String trangThai);
    
    /**
     * Check if user has any orders
     * @param maTaiKhoan User account ID
     * @return true if user has orders
     */
    boolean existsByMaTaiKhoan(Long maTaiKhoan);
    
    /**
     * Count orders by user ID
     * @param maTaiKhoan User account ID
     * @return Number of orders
     */
    long countByMaTaiKhoan(Long maTaiKhoan);
}
