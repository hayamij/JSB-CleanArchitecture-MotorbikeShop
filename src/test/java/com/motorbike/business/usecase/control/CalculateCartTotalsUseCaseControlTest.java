package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsInputData;
import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsOutputData;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateCartTotalsUseCaseControlTest {

    private CalculateCartTotalsUseCaseControl useCase = new CalculateCartTotalsUseCaseControl(null);

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
        CalculateCartTotalsOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getTotalItems());
        assertEquals(5, outputData.getTotalQuantity());
        // tamTinh = product1.giaBan * 2 + product2.giaBan * 3 = 90000000 + 1500000 = 91500000
        assertEquals(new BigDecimal("91500000"), outputData.getTotalAmount());
    }

    @Test
    void shouldHandleEmptyCart() {
        // Given
        List<ChiTietGioHang> cartItems = Collections.emptyList();
        CalculateCartTotalsInputData inputData = new CalculateCartTotalsInputData(cartItems);

        // When
        CalculateCartTotalsOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(0, outputData.getTotalItems());
        assertEquals(0, outputData.getTotalQuantity());
        assertEquals(BigDecimal.ZERO, outputData.getTotalAmount());
    }
}
