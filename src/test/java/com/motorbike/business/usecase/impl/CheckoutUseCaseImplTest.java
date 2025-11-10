package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.business.entity.Order;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.OrderRepository;
import com.motorbike.business.usecase.CheckoutUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutUseCaseImplTest {

    @Test
    void execute_shouldThrow_whenCartEmpty() {
        CartRepository cartRepo = mock(CartRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);
        when(cartRepo.findByUserId(10L)).thenReturn(Optional.empty());

        CheckoutUseCaseImpl useCase = new CheckoutUseCaseImpl(orderRepo, cartRepo);
        CheckoutUseCase.CheckoutRequest req = new CheckoutUseCase.CheckoutRequest(
                10L, "123 Street", "City", "0123456789", "COD");

        try {
            useCase.execute(req);
            fail("Should throw EmptyCartException");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("empty") || e.getMessage().contains("not found"));
        }
    }

    @Test
    void execute_shouldCreateOrder_whenCartHasItems() {
        CartRepository cartRepo = mock(CartRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);
        
        // Create cart with at least one item so isEmpty() returns false
        CartItem item = CartItem.builder()
                .productId(1L)
                .productName("Product1")
                .productPrice(new BigDecimal("5000"))
                .quantity(1)
                .subtotal(new BigDecimal("5000"))
                .build();
        Cart cart = Cart.builder()
                .userId(11L)
                .totalAmount(new BigDecimal("5000"))
                .items(Arrays.asList(item))
                .build();
        when(cartRepo.findByUserId(11L)).thenReturn(Optional.of(cart));
        Order savedOrder = Order.builder()
                .id(1L)
                .userId(11L)
                .totalAmount(new BigDecimal("5000"))
                .paymentMethod("ONLINE")
                .build();
        when(orderRepo.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutUseCaseImpl useCase = new CheckoutUseCaseImpl(orderRepo, cartRepo);
        CheckoutUseCase.CheckoutRequest req = new CheckoutUseCase.CheckoutRequest(
                11L, "456 Ave", "Town", "0987654321", "ONLINE");

        CheckoutUseCase.CheckoutResponse resp = useCase.execute(req);
        assertTrue(resp.isSuccess());
        verify(orderRepo).save(any(Order.class));
        verify(cartRepo).save(cart); // Cart is cleared and saved, not deleted
    }
}
