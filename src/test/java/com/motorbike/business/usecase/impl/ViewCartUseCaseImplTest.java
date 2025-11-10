package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.usecase.ViewCartUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewCartUseCaseImplTest {

    @Test
    void execute_shouldReturnEmptyMessage_whenNoCart() {
        CartRepository cartRepo = mock(CartRepository.class);
        when(cartRepo.findByUserId(7L)).thenReturn(Optional.empty());

        ViewCartUseCaseImpl useCase = new ViewCartUseCaseImpl(cartRepo);
        ViewCartUseCase.ViewCartRequest req = new ViewCartUseCase.ViewCartRequest(7L);

        ViewCartUseCase.ViewCartResponse resp = useCase.execute(req);
        assertTrue(resp.isEmpty());
        assertEquals("Your cart is empty", resp.getMessage());
    }

    @Test
    void execute_shouldReturnCartSummary_whenHasItems() {
        CartRepository cartRepo = mock(CartRepository.class);
        Cart cart = Cart.builder()
                .userId(8L)
                .totalAmount(new BigDecimal("3000"))
                .items(Arrays.asList(CartItem.builder().productId(1L).quantity(1).build()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(cartRepo.findByUserId(8L)).thenReturn(Optional.of(cart));

        ViewCartUseCaseImpl useCase = new ViewCartUseCaseImpl(cartRepo);
        ViewCartUseCase.ViewCartRequest req = new ViewCartUseCase.ViewCartRequest(8L);

        ViewCartUseCase.ViewCartResponse resp = useCase.execute(req);
        assertFalse(resp.isEmpty());
        assertTrue(resp.getMessage().contains("Cart has"));
    }
}
