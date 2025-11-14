package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface (Port) for DonHang (Order) Entity
 * Defines contract for order data access
 * Use case layer depends on this interface (Dependency Inversion)
 * Implementation will be in adapter/infrastructure layer
 */
public interface OrderRepository {
    
    /**
     * Save or update order
     * @param donHang DonHang entity to save
     * @return Saved DonHang entity with generated ID
     */
    DonHang save(DonHang donHang);
    
    /**
     * Find order by ID
     * @param orderId Order's ID
     * @return Optional containing order if found
     */
    Optional<DonHang> findById(Long orderId);
    
    /**
     * Find all orders by user ID
     * @param userId User's ID
     * @return List of orders belonging to the user
     */
    List<DonHang> findByUserId(Long userId);
    
    /**
     * Find orders by status
     * @param trangThai Order status
     * @return List of orders with the given status
     */
    List<DonHang> findByStatus(TrangThaiDonHang trangThai);
    
    /**
     * Find orders by user ID and status
     * @param userId User's ID
     * @param trangThai Order status
     * @return List of orders matching criteria
     */
    List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai);
    
    /**
     * Delete order by ID
     * @param orderId Order's ID
     */
    void deleteById(Long orderId);
    
    /**
     * Check if order exists by ID
     * @param orderId Order's ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long orderId);
}
