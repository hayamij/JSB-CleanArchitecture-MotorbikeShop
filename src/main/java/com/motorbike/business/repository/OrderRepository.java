package com.motorbike.business.repository;

import com.motorbike.business.entity.Order;
import java.util.List;
import java.util.Optional;

/**
 * Order Repository Interface - Business Layer
 * Defines contract for order data persistence
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public interface OrderRepository {
    
    /**
     * Save an order to the database
     * @param order order to save
     * @return saved order with ID
     */
    Order save(Order order);
    
    /**
     * Find order by ID
     * @param id order ID
     * @return optional containing order if found
     */
    Optional<Order> findById(Long id);
    
    /**
     * Find all orders by user ID
     * @param userId user ID
     * @return list of orders
     */
    List<Order> findByUserId(Long userId);
    
    /**
     * Find all orders
     * @return list of all orders
     */
    List<Order> findAll();
    
    /**
     * Delete an order by ID
     * @param id order ID
     */
    void deleteById(Long id);
}
