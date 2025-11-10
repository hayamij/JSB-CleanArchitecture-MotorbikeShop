package com.motorbike.persistence.adapter;

import com.motorbike.business.entity.Order;
import com.motorbike.business.repository.OrderRepository;
import com.motorbike.persistence.entity.OrderJpaEntity;
import com.motorbike.persistence.mapper.OrderMapper;
import com.motorbike.persistence.repository.OrderJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Order Repository Adapter - Persistence Layer
 * Adapter that implements OrderRepository using JPA
 * Part of Clean Architecture - Persistence Layer (adapter between business and frameworks)
 */
public class OrderRepositoryAdapter implements OrderRepository {
    
    private final OrderJpaRepository jpaRepository;
    
    public OrderRepositoryAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Order save(Order order) {
        OrderJpaEntity jpaEntity = OrderMapper.toJpaEntity(order);
        OrderJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return OrderMapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
                .map(OrderMapper::toDomainEntity);
    }
    
    @Override
    public List<Order> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(OrderMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(OrderMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
