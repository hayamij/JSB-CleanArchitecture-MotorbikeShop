package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.TaiKhoanJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for TaiKhoanJpaEntity
 * Provides CRUD operations and custom queries for user accounts
 */
@Repository
public interface TaiKhoanJpaRepository extends JpaRepository<TaiKhoanJpaEntity, Long> {
    
    /**
     * Find user by email
     * @param email User's email
     * @return Optional containing user if found
     */
    Optional<TaiKhoanJpaEntity> findByEmail(String email);
    
    /**
     * Find user by username
     * @param tenDangNhap Username
     * @return Optional containing user if found
     */
    Optional<TaiKhoanJpaEntity> findByTenDangNhap(String tenDangNhap);
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if username exists
     * @param tenDangNhap Username to check
     * @return true if exists
     */
    boolean existsByTenDangNhap(String tenDangNhap);
}
