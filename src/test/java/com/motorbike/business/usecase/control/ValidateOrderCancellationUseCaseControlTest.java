package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationInputData;
import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationOutputData;
import com.motorbike.domain.entities.TrangThaiDonHang;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateOrderCancellationUseCaseControlTest {

    @Test
    void shouldAllowCancellationForPendingOrder() {
        // Given - Order with CHO_XAC_NHAN status
        Long orderId = 1L;
        TrangThaiDonHang status = TrangThaiDonHang.CHO_XAC_NHAN;
        
        ValidateOrderCancellationInputData inputData = new ValidateOrderCancellationInputData(orderId, status);
        ValidateOrderCancellationUseCaseControl useCase = new ValidateOrderCancellationUseCaseControl(null);

        // When
        ValidateOrderCancellationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertTrue(outputData.canCancel());
        assertNotNull(outputData.getReason());
    }

    @Test
    void shouldDenyCancellationForShippingOrder() {
        // Given - Order with DANG_GIAO status
        Long orderId = 1L;
        TrangThaiDonHang status = TrangThaiDonHang.DANG_GIAO;
        
        ValidateOrderCancellationInputData inputData = new ValidateOrderCancellationInputData(orderId, status);
        ValidateOrderCancellationUseCaseControl useCase = new ValidateOrderCancellationUseCaseControl(null);

        // When
        ValidateOrderCancellationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.canCancel());
        assertNotNull(outputData.getReason());
    }

    @Test
    void shouldDenyCancellationForDeliveredOrder() {
        // Given - Order with DA_GIAO status
        Long orderId = 1L;
        TrangThaiDonHang status = TrangThaiDonHang.DA_GIAO;
        
        ValidateOrderCancellationInputData inputData = new ValidateOrderCancellationInputData(orderId, status);
        ValidateOrderCancellationUseCaseControl useCase = new ValidateOrderCancellationUseCaseControl(null);

        // When
        ValidateOrderCancellationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.canCancel());
        assertNotNull(outputData.getReason());
    }

    @Test
    void shouldAllowCancellationForConfirmedOrder() {
        // Given - Order with DA_XAC_NHAN status cannot be cancelled (already confirmed)
        Long orderId = 1L;
        TrangThaiDonHang status = TrangThaiDonHang.DA_XAC_NHAN;
        
        ValidateOrderCancellationInputData inputData = new ValidateOrderCancellationInputData(orderId, status);
        ValidateOrderCancellationUseCaseControl useCase = new ValidateOrderCancellationUseCaseControl(null);

        // When
        ValidateOrderCancellationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.canCancel());
        assertNotNull(outputData.getReason());
    }
}
