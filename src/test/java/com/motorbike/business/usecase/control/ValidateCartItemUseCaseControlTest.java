package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validatecartitem.ValidateCartItemInputData;
import com.motorbike.business.dto.validatecartitem.ValidateCartItemOutputData;
import com.motorbike.business.usecase.output.ValidateCartItemOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.business.ports.SanPhamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateCartItemUseCaseControlTest {

    @Mock
    private SanPhamRepository sanPhamRepository;

    @Mock
    private ValidateCartItemOutputBoundary outputBoundary;

    private ValidateCartItemUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateCartItemUseCaseControl(sanPhamRepository, outputBoundary);
    }

    @Test
    void shouldValidateCartItemSuccessfully() {
        // Given
        Long productId = 1L;
        int quantity = 5;
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10, true, false);
        product.setMaSP(productId);

        when(sanPhamRepository.findById(productId)).thenReturn(Optional.of(product));

        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, quantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateCartItemOutputData.class));
        verify(sanPhamRepository).findById(productId);
    }

    @Test
    void shouldFailWhenProductNotFound() {
        // Given
        Long productId = 999L;
        int quantity = 5;

        when(sanPhamRepository.findById(productId)).thenReturn(Optional.empty());

        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, quantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateCartItemOutputData.class));
        verify(sanPhamRepository).findById(productId);
    }

    @Test
    void shouldFailWhenInsufficientStock() {
        // Given
        Long productId = 1L;
        int quantity = 100; // More than available
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10, true, false);
        product.setMaSP(productId);

        when(sanPhamRepository.findById(productId)).thenReturn(Optional.of(product));

        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, quantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateCartItemOutputData.class));
    }

    @Test
    void shouldFailWhenProductNotVisible() {
        // Given
        Long productId = 1L;
        int quantity = 5;
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10, false, false);
        product.setMaSP(productId);

        when(sanPhamRepository.findById(productId)).thenReturn(Optional.of(product));

        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, quantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateCartItemOutputData.class));
    }

    @Test
    void shouldFailWhenQuantityIsZeroOrNegative() {
        // Given
        Long productId = 1L;
        int quantity = 0;

        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, quantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateCartItemOutputData.class));
        verifyNoInteractions(sanPhamRepository);
    }
}
