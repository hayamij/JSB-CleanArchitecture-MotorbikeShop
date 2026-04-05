package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.ChiTietDonHang;
import java.util.List;
import java.util.Optional;

public interface ChiTietDonHangRepository {
    
    /**
     * Find order detail by ID
     */
    Optional<ChiTietDonHang> findById(Long id);
    
    /**
     * Save order detail
     */
    ChiTietDonHang save(ChiTietDonHang chiTietDonHang);
    
    /**
     * Find all order details by order ID
     */
    List<ChiTietDonHang> findByDonHangId(Long donHangId);
    
    /**
     * Find all order details
     */
    List<ChiTietDonHang> findAll();
    
    /**
     * Delete order detail by ID
     */
    void deleteById(Long id);
    
    /**
     * Check if order detail exists by ID
     */
    boolean existsById(Long id);
}
