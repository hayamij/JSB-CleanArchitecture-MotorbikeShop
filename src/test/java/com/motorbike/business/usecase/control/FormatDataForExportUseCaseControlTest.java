package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatdataforexport.FormatDataForExportInputData;
import com.motorbike.business.dto.formatdataforexport.FormatDataForExportOutputData;
import com.motorbike.business.usecase.output.FormatDataForExportOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormatDataForExportUseCaseControlTest {

    @Mock
    private FormatDataForExportOutputBoundary outputBoundary;

    private FormatDataForExportUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new FormatDataForExportUseCaseControl(outputBoundary);
    }

    @Test
    void shouldFormatDataSuccessfully() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        List<SanPham> products = Arrays.asList(product1, product2);
        FormatDataForExportInputData inputData = new FormatDataForExportInputData(products);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(FormatDataForExportOutputData.class));
    }

    @Test
    void shouldHandleEmptyList() {
        // Given
        List<SanPham> products = Arrays.asList();
        FormatDataForExportInputData inputData = new FormatDataForExportInputData(products);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(FormatDataForExportOutputData.class));
    }
}
