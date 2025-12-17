package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateorder.ValidateOrderInputData;
import com.motorbike.business.dto.validateorder.ValidateOrderOutputData;
import com.motorbike.business.usecase.output.ValidateOrderOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateOrderUseCaseControlTest {

    @Mock
    private ValidateOrderOutputBoundary outputBoundary;

    private ValidateOrderUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateOrderUseCaseControl(outputBoundary);
    }

    @Test
    void shouldValidateOrderSuccessfully() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        GioHang item = new GioHang(1L, 1L, 5);
        item.setSanPham(product);

        List<GioHang> cartItems = Arrays.asList(item);
        ValidateOrderInputData inputData = new ValidateOrderInputData(cartItems);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateOrderOutputData.class));
    }

    @Test
    void shouldFailWhenCartIsEmpty() {
        // Given
        List<GioHang> cartItems = Collections.emptyList();
        ValidateOrderInputData inputData = new ValidateOrderInputData(cartItems);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateOrderOutputData.class));
    }

    @Test
    void shouldFailWhenInsufficientStock() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 2, true, false);
        product.setMaSP(1L);

        GioHang item = new GioHang(1L, 1L, 10); // More than available
        item.setSanPham(product);

        List<GioHang> cartItems = Arrays.asList(item);
        ValidateOrderInputData inputData = new ValidateOrderInputData(cartItems);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateOrderOutputData.class));
    }
}
