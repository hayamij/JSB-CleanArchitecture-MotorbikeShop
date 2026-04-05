package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.GioHang;
import java.util.Optional;

public interface CartRepository {
    
    
    Optional<GioHang> findByUserId(Long userId);
    
    
    Optional<GioHang> findById(Long cartId);
    
    
    GioHang save(GioHang gioHang);
    
    
    void delete(Long cartId);
    
    void deleteById(Long cartId);
    
    void deleteAllByUserId(Long userId);
    
    Optional<GioHang> findByUserIdAndProductId(Long userId, Long productId);
    
    int mergeGuestCartToUserCart(Long guestCartId, Long userCartId);
}
