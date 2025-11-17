package com.motorbike.infrastructure.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.motorbike.infrastructure.persistence.jpa.entities.DonHangJpaEntity;

/**
 * Spring Data JPA Repository for DonHangJpaEntity
 * Provides CRUD operations and custom queries for orders
 */
@Repository
public interface DonHangJpaRepository extends JpaRepository<DonHangJpaEntity, Long> {
    
    /**
     * Find all orders by user ID (sorted by date - newest first)
     */
    @Query("SELECT d FROM DonHangJpaEntity d WHERE d.maTaiKhoan = :userId ORDER BY d.ngayDat DESC")
    List<DonHangJpaEntity> findByMaTaiKhoan(@Param("userId") Long userId);
    
    /**
     * Find orders by status
     */
    List<DonHangJpaEntity> findByTrangThai(String trangThai);
    
    /**
     * Find orders by user ID and status
     */
    @Query("SELECT d FROM DonHangJpaEntity d WHERE d.maTaiKhoan = :userId AND d.trangThai = :trangThai ORDER BY d.ngayDat DESC")
    List<DonHangJpaEntity> findByMaTaiKhoanAndTrangThai(
            @Param("userId") Long userId,
            @Param("trangThai") String trangThai);
    
    /**
     * Check if user has any orders
     */
    boolean existsByMaTaiKhoan(Long userId);
    
    /**
     * Count orders by user ID
     */
    long countByMaTaiKhoan(Long userId);
    
    /**
     * Count orders by user ID and status
     */
    long countByMaTaiKhoanAndTrangThai(Long userId, String trangThai);
}