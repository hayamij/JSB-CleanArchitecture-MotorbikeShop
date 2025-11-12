package com.motorbike.domain.repositories;

import com.motorbike.domain.entities.Order;
import java.util.List;
import java.util.Optional;

/**
 * Repository Interface: OrderRepository
 * Defines data access methods for Order entity
 */
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findAll();
    void deleteById(Long id);
}
