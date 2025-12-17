package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.GioHang;
import java.util.List;
import java.util.Optional;

public interface GioHangRepository {
    
    /**
     * Find cart by ID
     */
    Optional<GioHang> findById(Long id);
    
    /**
     * Find cart by user ID and product ID
     */
    Optional<GioHang> findByMaTaiKhoanAndMaSanPham(Long maTaiKhoan, Long maSanPham);
    
    /**
     * Save cart
     */
    GioHang save(GioHang gioHang);
    
    /**
     * Find all cart items by user ID
     */
    List<GioHang> findByMaTaiKhoan(Long maTaiKhoan);
    
    /**
     * Find all cart items
     */
    List<GioHang> findAll();
    
    /**
     * Delete cart by ID
     */
    void deleteById(Long id);
    
    /**
     * Delete all cart items by user ID
     */
    void deleteByMaTaiKhoan(Long maTaiKhoan);
    
    /**
     * Check if cart exists by ID
     */
    boolean existsById(Long id);
}
