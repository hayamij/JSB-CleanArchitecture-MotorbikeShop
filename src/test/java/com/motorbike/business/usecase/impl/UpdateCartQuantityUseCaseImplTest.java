package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Cart;
import com.motorbike.business.entity.CartItem;
import com.motorbike.business.entity.Product;
import com.motorbike.business.exception.CartItemNotFoundException;
import com.motorbike.business.repository.CartRepository;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.business.usecase.UpdateCartQuantityUseCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCartQuantityUseCaseImplTest {

    @Test
    void execute_shouldThrow_whenItemNotFound() {
        CartRepository cartRepo = mock(CartRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        Cart cart = Cart.builder().userId(9L).items(Arrays.asList()).build();
        when(cartRepo.findByUserId(9L)).thenReturn(Optional.of(cart));

        UpdateCartQuantityUseCaseImpl useCase = new UpdateCartQuantityUseCaseImpl(cartRepo, productRepo);
        UpdateCartQuantityUseCase.UpdateCartQuantityRequest req = new UpdateCartQuantityUseCase.UpdateCartQuantityRequest(9L, 5L, 2);

        assertThrows(CartItemNotFoundException.class, () -> useCase.execute(req));
    }

    @Test
    void execute_shouldUpdateQuantity_whenItemExists() {
        CartRepository cartRepo = mock(CartRepository.class);
        ProductRepository productRepo = mock(ProductRepository.class);
        
        Product product = new Product();
        product.setId(5L);
        product.setName("Product5");
        product.setStockQuantity(100);
        when(productRepo.findById(5L)).thenReturn(Optional.of(product));
        
        CartItem item = CartItem.builder().productId(5L).quantity(1).build();
        Cart cart = Cart.builder().userId(9L).items(Arrays.asList(item)).build();
        when(cartRepo.findByUserId(9L)).thenReturn(Optional.of(cart));

        UpdateCartQuantityUseCaseImpl useCase = new UpdateCartQuantityUseCaseImpl(cartRepo, productRepo);
        UpdateCartQuantityUseCase.UpdateCartQuantityRequest req = new UpdateCartQuantityUseCase.UpdateCartQuantityRequest(9L, 5L, 3);

        useCase.execute(req);
        assertEquals(3, cart.getItems().get(0).getQuantity());
        verify(cartRepo).save(cart);
    }
}
