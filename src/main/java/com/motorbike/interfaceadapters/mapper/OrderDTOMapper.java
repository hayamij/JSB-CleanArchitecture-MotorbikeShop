package com.motorbike.interfaceadapters.mapper;

import com.motorbike.business.entity.Order;
import com.motorbike.business.entity.OrderItem;
import com.motorbike.interfaceadapters.dto.OrderDTO;
import com.motorbike.interfaceadapters.dto.OrderItemDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order DTO Mapper - Interface Adapters Layer
 * Maps between domain entities and DTOs
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class OrderDTOMapper {
    
    /**
     * Convert domain Order to DTO
     * @param order domain order
     * @return order DTO
     */
    public static OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }
        
        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        if (order.getItems() != null) {
            itemDTOs = order.getItems().stream()
                    .map(OrderDTOMapper::toItemDTO)
                    .collect(Collectors.toList());
        }
        
        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                itemDTOs,
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getShippingCity(),
                order.getShippingPhone(),
                order.getPaymentMethod(),
                order.getStatus(),
                order.getOrderDate(),
                order.getUpdatedAt()
        );
    }
    
    /**
     * Convert domain OrderItem to DTO
     * @param item domain order item
     * @return order item DTO
     */
    public static OrderItemDTO toItemDTO(OrderItem item) {
        if (item == null) {
            return null;
        }
        
        return new OrderItemDTO(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getProductPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}
