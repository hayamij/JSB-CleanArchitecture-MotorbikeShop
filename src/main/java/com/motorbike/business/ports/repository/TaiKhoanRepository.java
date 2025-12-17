package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.TaiKhoan;
import java.util.List;
import java.util.Optional;

public interface TaiKhoanRepository {
    
    /**
     * Find account by ID
     */
    Optional<TaiKhoan> findById(Long id);
    
    /**
     * Find account by username
     */
    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);
    
    /**
     * Find account by email
     */
    Optional<TaiKhoan> findByEmail(String email);
    
    /**
     * Save account
     */
    TaiKhoan save(TaiKhoan taiKhoan);
    
    /**
     * Find all accounts
     */
    List<TaiKhoan> findAll();
    
    /**
     * Delete account by ID
     */
    void deleteById(Long id);
    
    /**
     * Check if account exists by ID
     */
    boolean existsById(Long id);
    
    /**
     * Check if username exists
     */
    boolean existsByTenDangNhap(String tenDangNhap);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}
