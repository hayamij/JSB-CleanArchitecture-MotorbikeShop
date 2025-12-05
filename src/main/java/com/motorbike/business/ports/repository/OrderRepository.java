package com.motorbike.business.ports.repository;

import java.util.List;
import java.util.Optional;

import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

public interface OrderRepository {
    
    
    DonHang save(DonHang donHang);
    
    
    Optional<DonHang> findById(Long orderId);
    
    
    List<DonHang> findByUserId(Long userId);
    
    
    List<DonHang> findByStatus(TrangThaiDonHang trangThai);
    
    
    List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai);

    
    List<DonHang> findAll();
    
    
    void deleteById(Long orderId);
    
    
    boolean existsById(Long orderId);
    
    
    List<DonHang> searchOrders(String keyword);
}
