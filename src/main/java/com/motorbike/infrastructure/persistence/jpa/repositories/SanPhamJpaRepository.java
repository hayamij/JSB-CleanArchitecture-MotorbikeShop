package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.SanPhamJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamJpaRepository extends JpaRepository<SanPhamJpaEntity, Long> {
    
    
    List<SanPhamJpaEntity> findByLoaiSanPham(String loaiSanPham);
    
    
    List<SanPhamJpaEntity> findByConHangTrue();
    
    
    List<SanPhamJpaEntity> findByTenSanPhamContainingIgnoreCase(String keyword);
    
    
    List<SanPhamJpaEntity> findByLoaiSanPhamAndConHang(String loaiSanPham, boolean conHang);
    
    
    @Query("SELECT p FROM SanPhamJpaEntity p WHERE p.soLuongTonKho <= :threshold AND p.conHang = true")
    List<SanPhamJpaEntity> findLowStockProducts(@Param("threshold") int threshold);
}
