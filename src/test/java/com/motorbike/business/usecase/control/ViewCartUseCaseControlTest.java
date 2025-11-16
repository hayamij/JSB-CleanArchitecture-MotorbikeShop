package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("View Cart Use Case Tests")
class ViewCartUseCaseControlTest {

    private ViewCartUseCaseControl useCase;
    private CartRepository cartRepository;
    private ViewCartOutputBoundary outputBoundary;
    private ArgumentCaptor<ViewCartOutputData> outputCaptor;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(ViewCartOutputBoundary.class);
        outputCaptor = ArgumentCaptor.forClass(ViewCartOutputData.class);
        useCase = new ViewCartUseCaseControl(outputBoundary, cartRepository);
    }

    @Test
    @DisplayName("Should view cart successfully for logged-in user")
    void testViewCartSuccessLoggedInUser() {
        // Arrange
        Long userId = 1L;
        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(1L);
        cart.themSanPham(new ChiTietGioHang(
            1L, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        // Act
        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(userId);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        ViewCartOutputData output = outputCaptor.getValue();
        
        assertEquals(true, output.isSuccess());
        assertEquals(true, output.getCartId() != null);
        assertEquals(true, output.getTotalItems() >= 0);
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
        verify(outputBoundary).present(outputCaptor.capture());
        ViewCartOutputData output = outputCaptor.getValue();
        
        assertEquals(true, output.isSuccess());
        assertEquals(0, output.getTotalItems());
    }

    @Test
    @DisplayName("Should fail with null input")
    void testViewCartFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        ViewCartOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertNotEquals(null, output.getErrorCode());
    }
}
