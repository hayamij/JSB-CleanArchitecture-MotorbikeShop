package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.GioHangJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GioHangJpaRepository extends JpaRepository<GioHangJpaEntity, Long> {
    
    
    Optional<GioHangJpaEntity> findByMaTaiKhoan(Long maTaiKhoan);
    
    
    boolean existsByMaTaiKhoan(Long maTaiKhoan);
    
    
    @Query("SELECT g FROM GioHangJpaEntity g LEFT JOIN FETCH g.danhSachSanPham WHERE g.maGioHang = :maGioHang")
    Optional<GioHangJpaEntity> findByIdWithItems(@Param("maGioHang") Long maGioHang);
    
    
    @Query("SELECT g FROM GioHangJpaEntity g LEFT JOIN FETCH g.danhSachSanPham WHERE g.maTaiKhoan = :maTaiKhoan")
    Optional<GioHangJpaEntity> findByUserIdWithItems(@Param("maTaiKhoan") Long maTaiKhoan);
}
