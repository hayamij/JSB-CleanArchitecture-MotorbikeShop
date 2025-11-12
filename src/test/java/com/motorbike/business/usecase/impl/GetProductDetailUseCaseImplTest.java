package com.motorbike.business.usecase.impl;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Get Product Detail Use Case Tests")
class GetProductDetailUseCaseImplTest {

    private GetProductDetailUseCaseImpl useCase;
    private ProductRepository productRepository;
    private GetProductDetailOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        outputBoundary = mock(GetProductDetailOutputBoundary.class);
        useCase = new GetProductDetailUseCaseImpl(outputBoundary, productRepository);
    }

    @Test
    @DisplayName("Should get product detail successfully")
    void testGetProductDetailSuccess() {
        // Arrange
        Long productId = 1L;
        XeMay product = new XeMay(
            productId, "Honda Wave", "Xe số tiết kiệm",
            new BigDecimal("30000000"), "wave.jpg", 10, true,
            java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
            "Honda", "Wave RSX", "Đỏ", 2023, 110
        );
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        useCase.execute(new GetProductDetailInputData(productId));
        
        // Assert
        verify(productRepository).findById(productId);
        verify(outputBoundary).present(any(GetProductDetailOutputData.class));
    }

    @Test
    @DisplayName("Should fail when product not found")
    void testGetProductDetailFailNotFound() {
        // Arrange
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // Act
        useCase.execute(new GetProductDetailInputData(productId));
        
        // Assert
        verify(productRepository).findById(productId);
        verify(outputBoundary).present(any(GetProductDetailOutputData.class));
    }

    @Test
    @DisplayName("Should fail with null input")
    void testGetProductDetailFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(productRepository, never()).findById(any());
        verify(outputBoundary).present(any(GetProductDetailOutputData.class));
    }
}
