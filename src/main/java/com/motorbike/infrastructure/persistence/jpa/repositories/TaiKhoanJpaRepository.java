package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.TaiKhoanJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanJpaRepository extends JpaRepository<TaiKhoanJpaEntity, Long> {
    
    
    Optional<TaiKhoanJpaEntity> findByEmail(String email);
    
    
    Optional<TaiKhoanJpaEntity> findByTenDangNhap(String tenDangNhap);
    
    
    Optional<TaiKhoanJpaEntity> findBySoDienThoai(String soDienThoai);
    
    
    boolean existsByEmail(String email);
    
    
    boolean existsByTenDangNhap(String tenDangNhap);
}
