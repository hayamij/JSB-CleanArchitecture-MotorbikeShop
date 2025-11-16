package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
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
class GetProductDetailUseCaseControlTest {

    private GetProductDetailUseCaseControl useCase;
    private ProductRepository productRepository;
    private GetProductDetailOutputBoundary outputBoundary;
    private ArgumentCaptor<GetProductDetailOutputData> outputCaptor;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        outputBoundary = mock(GetProductDetailOutputBoundary.class);
        outputCaptor = ArgumentCaptor.forClass(GetProductDetailOutputData.class);
        useCase = new GetProductDetailUseCaseControl(outputBoundary, productRepository);
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
        verify(outputBoundary).present(outputCaptor.capture());
        GetProductDetailOutputData output = outputCaptor.getValue();
        
        assertEquals(true, output.success);
        assertEquals(productId, output.productId);
        assertEquals("Honda Wave", output.name);
        assertNotEquals(null, output.price);
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
        verify(outputBoundary).present(outputCaptor.capture());
        GetProductDetailOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.success);
        assertEquals("PRODUCT_NOT_FOUND", output.errorCode);
        assertNotEquals(null, output.errorMessage);
    }

    @Test
    @DisplayName("Should fail with null input")
    void testGetProductDetailFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        GetProductDetailOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.success);
        assertNotEquals(null, output.errorCode);
    }
}
