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

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Add To Cart Use Case Tests")
class AddToCartUseCaseControlTest {

    private AddToCartUseCaseControl useCase;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private AddToCartOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(AddToCartOutputBoundary.class);
        useCase = new AddToCartUseCaseControl(outputBoundary, cartRepository, productRepository);
    }

    @Test
    @DisplayName("Should add to cart successfully for logged-in user")
    void testAddToCartSuccessLoggedInUser() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        
        XeMay product = new XeMay(
            productId, "Honda Wave", "Xe số",
            new BigDecimal("30000000"), "wave.jpg", 10, true,
            java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
            "Honda", "Wave", "Đỏ", 2023, 110
        );
        
        GioHang cart = new GioHang(userId);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(GioHang.class))).thenReturn(cart);
        
        // Act
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(productId, 2, userId);
        useCase.execute(inputData);
        
        // Assert
        verify(productRepository).findById(productId);
        verify(cartRepository).findByUserId(userId);
        verify(cartRepository).save(any(GioHang.class));
        verify(outputBoundary).present(any(AddToCartOutputData.class));
    }

    @Test
    @DisplayName("Should fail when product not found")
    void testAddToCartFailProductNotFound() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Act
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(999L, 1, 1L);
        useCase.execute(inputData);
        
        // Assert
        verify(cartRepository, never()).save(any());
        verify(outputBoundary).present(any(AddToCartOutputData.class));
    }

    @Test
    @DisplayName("Should fail with null input")
    void testAddToCartFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(productRepository, never()).findById(any());
        verify(outputBoundary).present(any(AddToCartOutputData.class));
    }
}
