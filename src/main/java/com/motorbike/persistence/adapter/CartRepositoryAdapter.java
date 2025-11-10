package com.motorbike.persistence.adapter;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.persistence.entity.CartJpaEntity;
import com.motorbike.persistence.mapper.CartMapper;
import com.motorbike.persistence.repository.CartJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Cart Repository Adapter - Persistence Layer
 * Implements CartRepository interface using JPA repository
 * Part of Clean Architecture - Persistence Layer (adapter pattern)
 */
@Component
public class CartRepositoryAdapter implements CartRepository {
    
    private final CartJpaRepository cartJpaRepository;
    
    public CartRepositoryAdapter(CartJpaRepository cartJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
    }
    
    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartJpaRepository.findByUserId(userId)
                .map(CartMapper::toDomain);
    }
    
    @Override
    public Cart save(Cart cart) {
        CartJpaEntity jpaEntity = CartMapper.toJpaEntity(cart);
        CartJpaEntity saved = cartJpaRepository.save(jpaEntity);
        return CartMapper.toDomain(saved);
    }
    
    @Override
    public void deleteById(Long cartId) {
        cartJpaRepository.deleteById(cartId);
    }
    
    @Override
    public boolean existsByUserId(Long userId) {
        return cartJpaRepository.existsByUserId(userId);
    }
}
