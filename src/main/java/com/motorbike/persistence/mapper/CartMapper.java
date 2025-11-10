package com.motorbike.persistence.mapper;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.persistence.entity.CartItemJpaEntity;
import com.motorbike.persistence.entity.CartJpaEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cart Mapper - Persistence Layer
 * Maps between domain entities and JPA entities
 * Part of Clean Architecture - Persistence Layer
 */
public class CartMapper {
    
    /**
     * Convert domain Cart to JPA entity
     * @param cart domain cart
     * @return JPA cart entity
     */
    public static CartJpaEntity toJpaEntity(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        CartJpaEntity jpaEntity = new CartJpaEntity();
        jpaEntity.setId(cart.getId());
        jpaEntity.setUserId(cart.getUserId());
        jpaEntity.setTotalAmount(cart.getTotalAmount());
        jpaEntity.setCreatedAt(cart.getCreatedAt());
        jpaEntity.setUpdatedAt(cart.getUpdatedAt());
        
        // Map items with bidirectional relationship
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            List<CartItemJpaEntity> itemEntities = cart.getItems().stream()
                    .map(item -> {
                        CartItemJpaEntity itemEntity = new CartItemJpaEntity();
                        itemEntity.setId(item.getId());
                        itemEntity.setCart(jpaEntity); // Set parent reference
                        itemEntity.setProductId(item.getProductId());
                        itemEntity.setProductName(item.getProductName());
                        itemEntity.setProductPrice(item.getProductPrice());
                        itemEntity.setQuantity(item.getQuantity());
                        itemEntity.setSubtotal(item.getSubtotal());
                        itemEntity.setAddedAt(item.getAddedAt());
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            jpaEntity.setItems(itemEntities);
        }
        
        return jpaEntity;
    }
    
    /**
     * Convert JPA entity to domain Cart
     * @param jpaEntity JPA cart entity
     * @return domain cart
     */
    public static Cart toDomain(CartJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        List<CartItem> items = null;
        if (jpaEntity.getItems() != null) {
            items = jpaEntity.getItems().stream()
                    .map(CartMapper::toDomain)
                    .collect(Collectors.toList());
        }
        
        return Cart.builder()
                .id(jpaEntity.getId())
                .userId(jpaEntity.getUserId())
                .items(items)
                .totalAmount(jpaEntity.getTotalAmount())
                .createdAt(jpaEntity.getCreatedAt())
                .updatedAt(jpaEntity.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert JPA entity to domain CartItem
     * @param jpaEntity JPA cart item entity
     * @return domain cart item
     */
    public static CartItem toDomain(CartItemJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        return CartItem.builder()
                .id(jpaEntity.getId())
                .cartId(jpaEntity.getCartId())
                .productId(jpaEntity.getProductId())
                .productName(jpaEntity.getProductName())
                .productPrice(jpaEntity.getProductPrice())
                .quantity(jpaEntity.getQuantity())
                .subtotal(jpaEntity.getSubtotal())
                .addedAt(jpaEntity.getAddedAt())
                .build();
    }
}
