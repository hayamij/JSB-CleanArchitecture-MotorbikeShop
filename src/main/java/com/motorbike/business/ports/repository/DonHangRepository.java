package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import java.util.List;
import java.util.Optional;

public interface DonHangRepository {
    
    /**
     * Find order by ID
     */
    Optional<DonHang> findById(Long id);
    
    /**
     * Save order
     */
    DonHang save(DonHang donHang);
    
    /**
     * Find all orders
     */
    List<DonHang> findAll();
    
    /**
     * Find orders by user ID
     */
    List<DonHang> findByMaTaiKhoan(Long maTaiKhoan);
    
    /**
     * Find orders by status
     */
    List<DonHang> findByTrangThai(TrangThaiDonHang trangThai);
    
    /**
     * Delete order by ID
     */
    void deleteById(Long id);
    
    /**
     * Check if order exists by ID
     */
    boolean existsById(Long id);
}
