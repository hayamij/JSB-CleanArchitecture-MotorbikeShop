package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.ExportAccessoriesInputData;
import com.motorbike.business.usecase.output.ExportAccessoriesOutputBoundary;
import com.motorbike.business.usecase.input.GenerateExcelFileInputBoundary;
import com.motorbike.business.usecase.input.FormatDataForExportInputBoundary;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.domain.entities.PhuKienXeMay;
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
public class ExportAccessoriesUseCaseControlTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private GenerateExcelFileInputBoundary generateExcelFileUseCase;

    @Mock
    private FormatDataForExportInputBoundary formatDataForExportUseCase;

    @Mock
    private ExportAccessoriesOutputBoundary outputBoundary;

    private ExportAccessoriesUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ExportAccessoriesUseCaseControl(
            outputBoundary,
            accessoryRepository,
            formatDataForExportUseCase,
            generateExcelFileUseCase
        );
    }

    @Test
    void shouldExportAccessoriesSuccessfully() {
        // Given
        PhuKienXeMay accessory1 = new PhuKienXeMay("Mũ bảo hiểm", "Phụ tùng", 
            java.math.BigDecimal.valueOf(500000), "image1.jpg", 20, 
            "Mũ", "Honda", "Nhựa ABS", "L");
        accessory1.setMaSP(1L);

        PhuKienXeMay accessory2 = new PhuKienXeMay("Găng tay", "Phụ tùng", 
            java.math.BigDecimal.valueOf(200000), "image2.jpg", 30,
            "Găng tay", "Yamaha", "Da", "M");
        accessory2.setMaSP(2L);

        List<PhuKienXeMay> accessories = Arrays.asList(accessory1, accessory2);

        when(accessoryRepository.findAllAccessories()).thenReturn(accessories);

        ExportAccessoriesInputData inputData = new ExportAccessoriesInputData();

        // When
        useCase.execute(inputData);

        // Then
        verify(accessoryRepository).findAllAccessories();
        verify(formatDataForExportUseCase, atLeastOnce()).execute(any());
        verify(generateExcelFileUseCase, atLeastOnce()).execute(any());
    }

    @Test
    void shouldHandleEmptyAccessoryList() {
        // Given
        List<PhuKienXeMay> accessories = Arrays.asList();

        when(accessoryRepository.findAllAccessories()).thenReturn(accessories);

        ExportAccessoriesInputData inputData = new ExportAccessoriesInputData();

        // When
        useCase.execute(inputData);

        // Then
        verify(accessoryRepository).findAllAccessories();
    }
}
