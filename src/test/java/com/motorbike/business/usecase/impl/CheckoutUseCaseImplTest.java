package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.CheckoutOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Checkout Use Case Tests")
class CheckoutUseCaseImplTest {

    private CheckoutUseCaseImpl useCase;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CheckoutOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(CheckoutOutputBoundary.class);
        useCase = new CheckoutUseCaseImpl(outputBoundary, cartRepository, productRepository);
    }

    @Test
    @DisplayName("Should checkout successfully")
    void testCheckoutSuccess() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        
        GioHang cart = new GioHang(userId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        XeMay product = new XeMay(
            productId, "Honda Wave", "Xe số",
            new BigDecimal("30000000"), "wave.jpg", 10, true,
            java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
            "Honda", "Wave", "Đỏ", 2023, 110
        );
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        
        // Act
        CheckoutInputData inputData = new CheckoutInputData(userId, "123 Test St", "0123456789");
        useCase.execute(inputData);
        
        // Assert
        verify(cartRepository).findByUserId(userId);
        verify(productRepository, times(2)).findById(productId); // Called twice: validation + stock reduction
        verify(productRepository).save(any());
        verify(cartRepository).save(any(GioHang.class));
        verify(outputBoundary).present(any(CheckoutOutputData.class));
    }

    @Test
    @DisplayName("Should fail with empty cart")
    void testCheckoutFailEmptyCart() {
        // Arrange
        Long userId = 1L;
        GioHang cart = new GioHang(userId);
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        // Act
        CheckoutInputData inputData = new CheckoutInputData(userId, "123 Test St", "0123456789");
        useCase.execute(inputData);
        
        // Assert
        verify(productRepository, never()).save(any());
        verify(outputBoundary).present(any(CheckoutOutputData.class));
    }

    @Test
    @DisplayName("Should fail with insufficient stock")
    void testCheckoutFailInsufficientStock() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        
        GioHang cart = new GioHang(userId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), 10
        ));
        
        XeMay product = new XeMay(
            productId, "Honda Wave", "Xe số",
            new BigDecimal("30000000"), "wave.jpg", 5, true, // Only 5 in stock
            java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
            "Honda", "Wave", "Đỏ", 2023, 110
        );
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        CheckoutInputData inputData = new CheckoutInputData(userId, "123 Test St", "0123456789");
        useCase.execute(inputData);
        
        // Assert
        verify(productRepository, never()).save(any());
        verify(outputBoundary).present(any(CheckoutOutputData.class));
    }

    @Test
    @DisplayName("Should fail with null input")
    void testCheckoutFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(cartRepository, never()).findByUserId(any());
        verify(outputBoundary).present(any(CheckoutOutputData.class));
    }
}
