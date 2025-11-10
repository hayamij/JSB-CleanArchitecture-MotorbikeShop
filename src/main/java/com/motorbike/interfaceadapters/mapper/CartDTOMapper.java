package com.motorbike.interfaceadapters.mapper;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.interfaceadapters.dto.CartDTO;
import com.motorbike.interfaceadapters.dto.CartItemDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Cart Mapper - Interface Adapters Layer
 * Maps between domain entities and DTOs
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class CartDTOMapper {
    
    /**
     * Convert domain Cart to DTO
     * @param cart domain cart
     * @return cart DTO
     */
    public static CartDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        List<CartItemDTO> itemDTOs = null;
        if (cart.getItems() != null) {
            itemDTOs = cart.getItems().stream()
                    .map(CartDTOMapper::toDTO)
                    .collect(Collectors.toList());
        }
        
        return new CartDTO(
                cart.getId(),
                cart.getUserId(),
                itemDTOs,
                cart.getTotalAmount(),
                cart.getTotalItemCount(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
    
    /**
     * Convert domain CartItem to DTO
     * @param item domain cart item
     * @return cart item DTO
     */
    public static CartItemDTO toDTO(CartItem item) {
        if (item == null) {
            return null;
        }
        
        return new CartItemDTO(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getProductPrice(),
                item.getQuantity(),
                item.getSubtotal(),
                item.getAddedAt()
        );
    }
}
