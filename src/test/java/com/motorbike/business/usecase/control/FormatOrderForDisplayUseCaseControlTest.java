package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatorderfordisplay.FormatOrderForDisplayInputData;
import com.motorbike.business.dto.formatorderfordisplay.FormatOrderForDisplayOutputData;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.entities.PhuongThucThanhToan;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FormatOrderForDisplayUseCaseControlTest {

    @Test
    void shouldFormatOrderSuccessfully() {
        // Given
        DonHang order = new DonHang(
            1L,
            "Nguyễn Văn A",
            "0912345678",
            "123 Test Street",
            "Test Note",
            PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP
        );

        FormatOrderForDisplayInputData inputData = new FormatOrderForDisplayInputData(order);
        FormatOrderForDisplayUseCaseControl useCase = new FormatOrderForDisplayUseCaseControl(null);

        // When
        FormatOrderForDisplayOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotNull(outputData.getStatusText());
        assertNotNull(outputData.getFormattedAmount());
        assertNotNull(outputData.getStatusColor());
        assertEquals(1L, outputData.getOrderId());
    }

    @Test
    void shouldFormatOrderWithDifferentStatus() {
        // Given
        DonHang order = new DonHang(
            1L,
            "Trần Thị B",
            "0987654321",
            "456 Test Street",
            "Test Note",
            PhuongThucThanhToan.CHUYEN_KHOAN
        );
        order.setMaDonHang(2L);

        FormatOrderForDisplayInputData inputData = new FormatOrderForDisplayInputData(order);
        FormatOrderForDisplayUseCaseControl useCase = new FormatOrderForDisplayUseCaseControl(null);

        // When
        FormatOrderForDisplayOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2L, outputData.getOrderId());
        assertNotNull(outputData.getPaymentMethodText());
    }

    @Test
    void shouldHandleNullOrderInput() {
        // Given
        FormatOrderForDisplayInputData inputData = new FormatOrderForDisplayInputData(null);
        FormatOrderForDisplayUseCaseControl useCase = new FormatOrderForDisplayUseCaseControl(null);

        // When
        FormatOrderForDisplayOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotNull(outputData.getErrorCode());
    }
}
