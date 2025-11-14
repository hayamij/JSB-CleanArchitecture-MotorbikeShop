package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.SanPhamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for SanPhamJpaEntity
 * Provides CRUD operations and custom queries for products
 * Supports JOINED inheritance strategy (XeMay, PhuKienXeMay)
 */
@Repository
public interface SanPhamJpaRepository extends JpaRepository<SanPhamJpaEntity, Long> {
    
    /**
     * Find all products by type
     * @param loaiSanPham Product type (XE_MAY, PHU_KIEN)
     * @return List of products
     */
    List<SanPhamJpaEntity> findByLoaiSanPham(String loaiSanPham);
    
    /**
     * Find all available products (in stock)
     * @return List of available products
     */
    List<SanPhamJpaEntity> findByConHangTrue();
    
    /**
     * Find products by name containing (case-insensitive search)
     * @param keyword Search keyword
     * @return List of matching products
     */
    List<SanPhamJpaEntity> findByTenSanPhamContainingIgnoreCase(String keyword);
    
    /**
     * Find products by type and availability
     * @param loaiSanPham Product type
     * @param conHang Availability status
     * @return List of products
     */
    List<SanPhamJpaEntity> findByLoaiSanPhamAndConHang(String loaiSanPham, boolean conHang);
    
    /**
     * Custom query to find products with low stock
     * @param threshold Stock threshold
     * @return List of products with stock below threshold
     */
    @Query("SELECT p FROM SanPhamJpaEntity p WHERE p.soLuongTonKho <= :threshold AND p.conHang = true")
    List<SanPhamJpaEntity> findLowStockProducts(@Param("threshold") int threshold);
}
