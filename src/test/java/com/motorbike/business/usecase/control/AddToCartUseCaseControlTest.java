package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.GioHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Add To Cart Use Case Tests")
class AddToCartUseCaseControlTest {

    private AddToCartUseCaseControl useCase;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private AddToCartOutputBoundary outputBoundary;
    private ArgumentCaptor<AddToCartOutputData> outputCaptor;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(AddToCartOutputBoundary.class);
        outputCaptor = ArgumentCaptor.forClass(AddToCartOutputData.class);
        useCase = new AddToCartUseCaseControl(outputBoundary, cartRepository, productRepository);
    }

    @Test
    @DisplayName("Should add to cart successfully for logged-in user")
    void testAddToCartSuccessLoggedInUser() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        int quantity = 2;
        
        XeMay product = new XeMay(
            productId, "Honda Wave", "Xe số",
            new BigDecimal("30000000"), "wave.jpg", 10, true,
            java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
            "Honda", "Wave", "Đỏ", 2023, 110
        );
        
        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(1L);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(GioHang.class))).thenReturn(cart);
        
        // Act
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(productId, quantity, userId);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        AddToCartOutputData output = outputCaptor.getValue();
        
        assertEquals(true, output.isSuccess());
        assertEquals(true, output.getCartId() != null);
        assertNotEquals(0, output.getTotalItems());
    }

    @Test
    @DisplayName("Should fail when product not found")
    void testAddToCartFailProductNotFound() {
        // Arrange
        Long productId = 999L;
        
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // Act
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(productId, 1, 1L);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        AddToCartOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertEquals("PRODUCT_NOT_FOUND", output.getErrorCode());
        assertNotEquals(null, output.getErrorMessage());
    }

    @Test
    @DisplayName("Should fail with null input")
    void testAddToCartFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        AddToCartOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertNotEquals(null, output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with invalid quantity")
    void testAddToCartFailInvalidQuantity() {
        // Act
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 0, 1L);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        AddToCartOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertEquals("INVALID_QUANTITY", output.getErrorCode());
    }
}
