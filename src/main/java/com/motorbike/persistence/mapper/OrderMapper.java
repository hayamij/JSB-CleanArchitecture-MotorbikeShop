package com.motorbike.persistence.mapper;

import com.motorbike.business.entity.Order;
import com.motorbike.business.entity.OrderItem;
import com.motorbike.persistence.entity.OrderJpaEntity;
import com.motorbike.persistence.entity.OrderItemJpaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Order Mapper - Persistence Layer
 * Maps between domain entities and JPA entities
 * Part of Clean Architecture - Persistence Layer
 */
public class OrderMapper {
    
    /**
     * Convert domain Order to JPA entity
     * @param order domain order
     * @return JPA entity
     */
    public static OrderJpaEntity toJpaEntity(Order order) {
        if (order == null) {
            return null;
        }
        
        OrderJpaEntity jpaEntity = new OrderJpaEntity();
        jpaEntity.setId(order.getId());
        jpaEntity.setUserId(order.getUserId());
        jpaEntity.setTotalAmount(order.getTotalAmount());
        jpaEntity.setShippingAddress(order.getShippingAddress());
        jpaEntity.setShippingCity(order.getShippingCity());
        jpaEntity.setShippingPhone(order.getShippingPhone());
        jpaEntity.setPaymentMethod(order.getPaymentMethod());
        jpaEntity.setStatus(order.getStatus());
        jpaEntity.setOrderDate(order.getOrderDate());
        jpaEntity.setUpdatedAt(order.getUpdatedAt());
        
        // Convert order items
        if (order.getItems() != null) {
            List<OrderItemJpaEntity> itemEntities = order.getItems().stream()
                    .map(item -> toOrderItemJpaEntity(item, jpaEntity))
                    .collect(Collectors.toList());
            jpaEntity.setItems(itemEntities);
        }
        
        return jpaEntity;
    }
    
    /**
     * Convert JPA entity to domain Order
     * @param jpaEntity JPA entity
     * @return domain order
     */
    public static Order toDomainEntity(OrderJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        // Convert order items
        List<OrderItem> items = new ArrayList<>();
        if (jpaEntity.getItems() != null) {
            items = jpaEntity.getItems().stream()
                    .map(OrderMapper::toOrderItemDomainEntity)
                    .collect(Collectors.toList());
        }
        
        return Order.builder()
                .id(jpaEntity.getId())
                .userId(jpaEntity.getUserId())
                .items(items)
                .totalAmount(jpaEntity.getTotalAmount())
                .shippingAddress(jpaEntity.getShippingAddress())
                .shippingCity(jpaEntity.getShippingCity())
                .shippingPhone(jpaEntity.getShippingPhone())
                .paymentMethod(jpaEntity.getPaymentMethod())
                .status(jpaEntity.getStatus())
                .orderDate(jpaEntity.getOrderDate())
                .updatedAt(jpaEntity.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert domain OrderItem to JPA entity
     * @param item domain order item
     * @param orderEntity parent order JPA entity
     * @return JPA entity
     */
    private static OrderItemJpaEntity toOrderItemJpaEntity(OrderItem item, OrderJpaEntity orderEntity) {
        if (item == null) {
            return null;
        }
        
        OrderItemJpaEntity itemEntity = new OrderItemJpaEntity();
        itemEntity.setId(item.getId());
        itemEntity.setOrder(orderEntity);
        itemEntity.setProductId(item.getProductId());
        itemEntity.setProductName(item.getProductName());
        itemEntity.setProductPrice(item.getProductPrice());
        itemEntity.setQuantity(item.getQuantity());
        itemEntity.setSubtotal(item.getSubtotal());
        
        return itemEntity;
    }
    
    /**
     * Convert JPA entity to domain OrderItem
     * @param itemEntity JPA entity
     * @return domain order item
     */
    private static OrderItem toOrderItemDomainEntity(OrderItemJpaEntity itemEntity) {
        if (itemEntity == null) {
            return null;
        }
        
        return OrderItem.builder()
                .id(itemEntity.getId())
                .productId(itemEntity.getProductId())
                .productName(itemEntity.getProductName())
                .productPrice(itemEntity.getProductPrice())
                .quantity(itemEntity.getQuantity())
                .subtotal(itemEntity.getSubtotal())
                .build();
    }
}
