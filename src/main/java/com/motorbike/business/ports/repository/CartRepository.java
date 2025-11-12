package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.Cart;
import java.util.Optional;

/**
 * Repository Interface (Port) for Cart Entity
 * Defines contract for cart data access
 * Use case layer depends on this interface (Dependency Inversion)
 * Implementation will be in adapter/infrastructure layer
 */
public interface CartRepository {
    
    /**
     * Find cart by user ID
     * @param userId User's ID
     * @return Optional containing cart if found
     */
    Optional<Cart> findByUserId(Long userId);
    
    /**
     * Find cart by cart ID
     * @param cartId Cart's ID
     * @return Optional containing cart if found
     */
    Optional<Cart> findById(Long cartId);
    
    /**
     * Save or update cart
     * @param cart Cart entity to save
     * @return Saved cart with ID
     */
    Cart save(Cart cart);
    
    /**
     * Delete cart
     * @param cartId Cart's ID to delete
     */
    void delete(Long cartId);
    
    /**
     * Merge guest cart into user cart
     * @param guestCartId Guest cart ID
     * @param userCartId User cart ID
     * @return Number of items merged
     */
    int mergeGuestCartToUserCart(Long guestCartId, Long userCartId);
}
