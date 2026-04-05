package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayInputData;
import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayOutputData;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class FormatProductForDisplayUseCaseControlTest {

    @Test
    void shouldFormatMotorbikeProductSuccessfully() {
        // Given
        XeMay product = new XeMay(
            1L, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "exciter.jpg", 10, true,
            null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 155
        );

        FormatProductForDisplayInputData inputData = new FormatProductForDisplayInputData(product);
        FormatProductForDisplayUseCaseControl useCase = new FormatProductForDisplayUseCaseControl(null);

        // When
        FormatProductForDisplayOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(1L, outputData.getProductId());
        assertEquals("Yamaha Exciter", outputData.getProductName());
        assertNotNull(outputData.getFormattedPrice());
        assertNotNull(outputData.getStockStatus());
    }

    @Test
    void shouldFormatAccessoryProduct() {
        // Given
        PhuKienXeMay accessory = new PhuKienXeMay(
            2L, "Mũ bảo hiểm", "Mũ Fullface",
            BigDecimal.valueOf(500000), "helmet.jpg", 20, true,
            null, null,
            "Mũ", "Royal", "ABS", "L"
        );

        FormatProductForDisplayInputData inputData = new FormatProductForDisplayInputData(accessory);
        FormatProductForDisplayUseCaseControl useCase = new FormatProductForDisplayUseCaseControl(null);

        // When
        FormatProductForDisplayOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2L, outputData.getProductId());
        assertEquals("Mũ bảo hiểm", outputData.getProductName());
        assertNotNull(outputData.getCategoryDisplay());
    }

    @Test
    void shouldHandleNullProduct() {
        // Given
        FormatProductForDisplayInputData inputData = new FormatProductForDisplayInputData(null);
        FormatProductForDisplayUseCaseControl useCase = new FormatProductForDisplayUseCaseControl(null);

        // When
        FormatProductForDisplayOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotNull(outputData.getErrorCode());
    }
}
