package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.ViewCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("View Cart Use Case Tests")
class ViewCartUseCaseImplTest {

    private ViewCartUseCaseImpl useCase;
    private CartRepository cartRepository;
    private ViewCartOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(ViewCartOutputBoundary.class);
        useCase = new ViewCartUseCaseImpl(outputBoundary, cartRepository);
    }

    @Test
    @DisplayName("Should view cart successfully for logged-in user")
    void testViewCartSuccessLoggedInUser() {
        // Arrange
        Long userId = 1L;
        GioHang cart = new GioHang(userId);
        cart.themSanPham(new ChiTietGioHang(
            1L, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        // Act
        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(userId);
        useCase.execute(inputData);
        
        // Assert
        verify(cartRepository).findByUserId(userId);
        verify(outputBoundary).present(any(ViewCartOutputData.class));
    }

    @Test
    @DisplayName("Should handle empty cart")
    void testViewCartSuccessEmptyCart() {
        // Arrange
        Long userId = 1L;
        GioHang cart = new GioHang(userId);
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        // Act
        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(userId);
        useCase.execute(inputData);
        
        // Assert
        verify(cartRepository).findByUserId(userId);
        verify(outputBoundary).present(any(ViewCartOutputData.class));
    }

    @Test
    @DisplayName("Should fail with null input")
    void testViewCartFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(cartRepository, never()).findByUserId(any());
        verify(outputBoundary).present(any(ViewCartOutputData.class));
    }
}
