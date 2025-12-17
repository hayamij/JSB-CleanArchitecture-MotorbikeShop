package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsInputData;
import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsOutputData;
import com.motorbike.domain.entities.ChiTietDonHang;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateOrderTotalsUseCaseControlTest {

    private CalculateOrderTotalsUseCaseControl useCase = new CalculateOrderTotalsUseCaseControl(null);

    @Test
    void shouldCalculateTotalsSuccessfully() {
        // Given
        ChiTietDonHang detail1 = new ChiTietDonHang(1L, 1L, 2, 45000000.0);
        ChiTietDonHang detail2 = new ChiTietDonHang(1L, 2L, 3, 500000.0);

        List<ChiTietDonHang> orderDetails = Arrays.asList(detail1, detail2);
        CalculateOrderTotalsInputData inputData = new CalculateOrderTotalsInputData(orderDetails);

        // When
        CalculateOrderTotalsOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getTotalItems());
        assertEquals(5, outputData.getTotalQuantity());
        // thanhTien = detail1.donGia * 2 + detail2.donGia * 3 = 90000000 + 1500000 = 91500000
        assertEquals(new BigDecimal("91500000"), outputData.getTotalAmount());
    }

    @Test
    void shouldHandleEmptyOrderDetails() {
        // Given
        List<ChiTietDonHang> orderDetails = Collections.emptyList();
        CalculateOrderTotalsInputData inputData = new CalculateOrderTotalsInputData(orderDetails);

        // When
        CalculateOrderTotalsOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(0, outputData.getTotalItems());
        assertEquals(0, outputData.getTotalQuantity());
        assertEquals(BigDecimal.ZERO, outputData.getTotalAmount());
    }
}
