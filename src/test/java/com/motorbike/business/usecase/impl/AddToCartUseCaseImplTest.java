package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.business.entity.Product;
import com.motorbike.business.exception.ProductOutOfStockException;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.business.usecase.AddToCartUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddToCartUseCaseImplTest {

    @Test
    void execute_shouldAddItem_whenProductAvailable() {
        ProductRepository productRepo = mock(ProductRepository.class);
        CartRepository cartRepo = mock(CartRepository.class);

        Product p = new Product();
        p.setId(1L);
        p.setName("P1");
        p.setPrice(BigDecimal.valueOf(1000));
        p.setStockQuantity(10);
        p.setAvailable(true);

        when(productRepo.findById(1L)).thenReturn(Optional.of(p));
        when(cartRepo.findByUserId(5L)).thenReturn(Optional.empty());

        Cart saved = Cart.builder().userId(5L).build();
        when(cartRepo.save(any(Cart.class))).thenReturn(saved);

        AddToCartUseCaseImpl useCase = new AddToCartUseCaseImpl(cartRepo, productRepo);
        AddToCartUseCase.AddToCartRequest req = new AddToCartUseCase.AddToCartRequest(5L, 1L, 2);

        AddToCartUseCase.AddToCartResponse resp = useCase.execute(req);

        assertTrue(resp.isSuccess());
        assertEquals("Added 2 x P1 to cart successfully", resp.getMessage());
    }

    @Test
    void execute_shouldThrowOutOfStock_whenNotEnough() {
        ProductRepository productRepo = mock(ProductRepository.class);
        CartRepository cartRepo = mock(CartRepository.class);

        Product p = new Product();
        p.setId(2L);
        p.setName("P2");
        p.setPrice(BigDecimal.valueOf(2000));
        p.setStockQuantity(1);
        p.setAvailable(true);

        when(productRepo.findById(2L)).thenReturn(Optional.of(p));

        AddToCartUseCaseImpl useCase = new AddToCartUseCaseImpl(cartRepo, productRepo);
        AddToCartUseCase.AddToCartRequest req = new AddToCartUseCase.AddToCartRequest(5L, 2L, 5);

        assertThrows(ProductOutOfStockException.class, () -> useCase.execute(req));
    }
}
