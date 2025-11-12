package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Update Cart Quantity Use Case Tests")
class UpdateCartQuantityUseCaseControlTest {

    private UpdateCartQuantityUseCaseControl useCase;
    private CartRepository cartRepository;
    private UpdateCartQuantityOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(UpdateCartQuantityOutputBoundary.class);
        useCase = new UpdateCartQuantityUseCaseControl(outputBoundary, cartRepository);
    }

    @Test
    @DisplayName("Should update quantity successfully")
    void testUpdateQuantitySuccess() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        GioHang cart = new GioHang(userId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(GioHang.class))).thenReturn(cart);
        
        // Act
        UpdateCartQuantityInputData inputData = UpdateCartQuantityInputData.forLoggedInUser(userId, productId, 5);
        useCase.execute(inputData);
        
        // Assert
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(any(GioHang.class));
        verify(outputBoundary).present(any(UpdateCartQuantityOutputData.class));
    }

    @Test
    @DisplayName("Should remove item when quantity is zero")
    void testRemoveItemWhenQuantityZero() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        GioHang cart = new GioHang(userId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(GioHang.class))).thenReturn(cart);
        
        // Act
        UpdateCartQuantityInputData inputData = UpdateCartQuantityInputData.forLoggedInUser(userId, productId, 0);
        useCase.execute(inputData);
        
        // Assert
        verify(cartRepository).save(any(GioHang.class));
        verify(outputBoundary).present(any(UpdateCartQuantityOutputData.class));
    }

    @Test
    @DisplayName("Should fail with null input")
    void testUpdateQuantityFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(cartRepository, never()).findByUserId(any());
        verify(outputBoundary).present(any(UpdateCartQuantityOutputData.class));
    }
}
