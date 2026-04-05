package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatdataforexport.FormatDataForExportInputData;
import com.motorbike.business.dto.formatdataforexport.FormatDataForExportOutputData;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormatDataForExportUseCaseControlTest {

    private FormatDataForExportUseCaseControl useCase = new FormatDataForExportUseCaseControl(null);

    @Test
    void shouldFormatDataSuccessfully() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        List<SanPham> products = Arrays.asList(product1, product2);
        FormatDataForExportInputData inputData = new FormatDataForExportInputData(products, null);

        // When
        FormatDataForExportOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotEquals(null, outputData.getFormattedData());
        assertNotEquals(null, outputData.getHeaders());
        assertEquals(2, outputData.getRowCount());
    }

    @Test
    void shouldHandleEmptyList() {
        // Given
        List<SanPham> products = Collections.emptyList();
        FormatDataForExportInputData inputData = new FormatDataForExportInputData(products, null);

        // When
        FormatDataForExportOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(0, outputData.getRowCount());
    }

    @Test
    void shouldFormatMotorbikeData() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        List<SanPham> products = Arrays.asList(product);
        FormatDataForExportInputData inputData = new FormatDataForExportInputData(products, "motorbike");

        // When
        FormatDataForExportOutputData outputData = useCase.formatInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(1, outputData.getRowCount());
    }
}
