package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationInputData;
import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationOutputData;
import com.motorbike.business.usecase.output.ValidateOrderCancellationOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateOrderCancellationUseCaseControlTest {

    @Mock
    private ValidateOrderCancellationOutputBoundary outputBoundary;

    private ValidateOrderCancellationUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateOrderCancellationUseCaseControl(outputBoundary);
    }

    @Test
    void shouldAllowCancellationForPendingOrder() {
        // Given
        DonHang order = new DonHang(1L, 90000000.0, "PENDING");
        order.setMaDH(1L);

        ValidateOrderCancellationInputData inputData = new ValidateOrderCancellationInputData(order);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateOrderCancellationOutputData.class));
    }

    @Test
    void shouldDenyCancellationForShippedOrder() {
        // Given
        DonHang order = new DonHang(1L, 90000000.0, "SHIPPED");
        order.setMaDH(1L);

        ValidateOrderCancellationInputData inputData = new ValidateOrderCancellationInputData(order);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateOrderCancellationOutputData.class));
    }

    @Test
    void shouldDenyCancellationForDeliveredOrder() {
        // Given
        DonHang order = new DonHang(1L, 90000000.0, "DELIVERED");
        order.setMaDH(1L);

        ValidateOrderCancellationInputData inputData = new ValidateOrderCancellationInputData(order);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateOrderCancellationOutputData.class));
    }
}
