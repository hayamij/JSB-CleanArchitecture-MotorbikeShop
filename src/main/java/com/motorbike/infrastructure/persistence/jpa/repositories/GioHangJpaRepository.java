package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.GioHangJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for GioHangJpaEntity
 * Provides CRUD operations and custom queries for shopping carts
 */
@Repository
public interface GioHangJpaRepository extends JpaRepository<GioHangJpaEntity, Long> {
    
    /**
     * Find cart by user ID
     * @param maTaiKhoan User account ID
     * @return Optional containing cart if found
     */
    Optional<GioHangJpaEntity> findByMaTaiKhoan(Long maTaiKhoan);
    
    /**
     * Check if user has a cart
     * @param maTaiKhoan User account ID
     * @return true if cart exists
     */
    boolean existsByMaTaiKhoan(Long maTaiKhoan);
    
    /**
     * Find cart with items (fetch join for performance)
     * @param maGioHang Cart ID
     * @return Optional containing cart with items
     */
    @Query("SELECT g FROM GioHangJpaEntity g LEFT JOIN FETCH g.danhSachSanPham WHERE g.maGioHang = :maGioHang")
    Optional<GioHangJpaEntity> findByIdWithItems(@Param("maGioHang") Long maGioHang);
    
    /**
     * Find cart by user with items (fetch join for performance)
     * @param maTaiKhoan User account ID
     * @return Optional containing cart with items
     */
    @Query("SELECT g FROM GioHangJpaEntity g LEFT JOIN FETCH g.danhSachSanPham WHERE g.maTaiKhoan = :maTaiKhoan")
    Optional<GioHangJpaEntity> findByUserIdWithItems(@Param("maTaiKhoan") Long maTaiKhoan);
}
