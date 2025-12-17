package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsInputData;
import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsOutputData;
import com.motorbike.business.usecase.output.CalculateOrderTotalsOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
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
public class CalculateOrderTotalsUseCaseControlTest {

    @Mock
    private CalculateOrderTotalsOutputBoundary outputBoundary;

    private CalculateOrderTotalsUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CalculateOrderTotalsUseCaseControl(outputBoundary);
    }

    @Test
    void shouldCalculateTotalsSuccessfully() {
        // Given
        ChiTietDonHang detail1 = new ChiTietDonHang(1L, 1L, 2, 45000000.0);
        ChiTietDonHang detail2 = new ChiTietDonHang(1L, 2L, 3, 500000.0);

        List<ChiTietDonHang> orderDetails = Arrays.asList(detail1, detail2);
        CalculateOrderTotalsInputData inputData = new CalculateOrderTotalsInputData(orderDetails);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CalculateOrderTotalsOutputData.class));
    }

    @Test
    void shouldHandleEmptyOrderDetails() {
        // Given
        List<ChiTietDonHang> orderDetails = Collections.emptyList();
        CalculateOrderTotalsInputData inputData = new CalculateOrderTotalsInputData(orderDetails);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CalculateOrderTotalsOutputData.class));
    }
}
