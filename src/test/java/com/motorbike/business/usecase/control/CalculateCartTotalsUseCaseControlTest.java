package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsInputData;
import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsOutputData;
import com.motorbike.business.usecase.output.CalculateCartTotalsOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
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
public class CalculateCartTotalsUseCaseControlTest {

    @Mock
    private CalculateCartTotalsOutputBoundary outputBoundary;

    private CalculateCartTotalsUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CalculateCartTotalsUseCaseControl(outputBoundary);
    }

    @Test
    void shouldCalculateTotalsSuccessfully() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);
        
        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        ChiTietGioHang item1 = new ChiTietGioHang(1L, 1L, 2);
        item1.setSanPham(product1);
        
        ChiTietGioHang item2 = new ChiTietGioHang(1L, 2L, 3);
        item2.setSanPham(product2);

        List<ChiTietGioHang> cartItems = Arrays.asList(item1, item2);
        CalculateCartTotalsInputData inputData = new CalculateCartTotalsInputData(cartItems);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CalculateCartTotalsOutputData.class));
    }

    @Test
    void shouldHandleEmptyCart() {
        // Given
        List<ChiTietGioHang> cartItems = Collections.emptyList();
        CalculateCartTotalsInputData inputData = new CalculateCartTotalsInputData(cartItems);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CalculateCartTotalsOutputData.class));
    }
}
