package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.business.entity.Order;
import com.motorbike.business.entity.OrderItem;
import com.motorbike.business.exception.EmptyCartException;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.OrderRepository;
import com.motorbike.business.usecase.CheckoutUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CheckoutUseCaseImpl - Business Layer
 * Implementation of checkout use case
 * Part of Clean Architecture - Business Layer
 */
public class CheckoutUseCaseImpl implements CheckoutUseCase {
    
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    
    public CheckoutUseCaseImpl(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }
    
    @Override
    public CheckoutResponse execute(CheckoutRequest request) {
        // Step 1: Validate request
        validateRequest(request);
        
        // Step 2: Get cart by userId
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new EmptyCartException(request.getUserId()));
        
        // Step 3: Check if cart is empty
        if (cart.isEmpty()) {
            throw new EmptyCartException(request.getUserId());
        }
        
        // Step 4: Convert cart items to order items
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::convertCartItemToOrderItem)
                .collect(Collectors.toList());
        
        // Step 5: Create order entity
        Order order = Order.builder()
                .userId(request.getUserId())
                .items(orderItems)
                .shippingAddress(request.getShippingAddress())
                .shippingCity(request.getShippingCity())
                .shippingPhone(request.getShippingPhone())
                .paymentMethod(request.getPaymentMethod().toUpperCase())
                .status("PENDING")
                .orderDate(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        // Step 6: Validate order
        order.validate();
        
        // Step 7: Save order
        Order savedOrder = orderRepository.save(order);
        
        // Step 8: Clear cart after successful order
        cart.clear();
        cartRepository.save(cart);
        
        // Step 9: Generate success message
        String message = String.format(
                "Order placed successfully! Order ID: %d, Total: %s VND, Payment: %s",
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getPaymentMethod()
        );
        
        // Step 10: Return response
        return new CheckoutResponse(savedOrder, message, true);
    }
    
    /**
     * Validate checkout request
     */
    private void validateRequest(CheckoutRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (request.getShippingAddress() == null || request.getShippingAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address is required");
        }
        if (request.getShippingCity() == null || request.getShippingCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping city is required");
        }
        if (request.getShippingPhone() == null || request.getShippingPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping phone is required");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required");
        }
        String method = request.getPaymentMethod().toUpperCase();
        if (!method.equals("COD") && !method.equals("ONLINE")) {
            throw new IllegalArgumentException("Payment method must be COD or ONLINE");
        }
    }
    
    /**
     * Convert cart item to order item
     */
    private OrderItem convertCartItemToOrderItem(CartItem cartItem) {
        return OrderItem.builder()
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .productPrice(cartItem.getProductPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(cartItem.getSubtotal())
                .build();
    }
}
