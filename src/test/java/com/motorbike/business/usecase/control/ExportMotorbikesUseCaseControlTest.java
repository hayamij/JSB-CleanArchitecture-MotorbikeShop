package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.ExportMotorbikesInputData;
import com.motorbike.business.usecase.output.ExportMotorbikesOutputBoundary;
import com.motorbike.business.usecase.input.GenerateExcelFileInputBoundary;
import com.motorbike.business.usecase.input.FormatDataForExportInputBoundary;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.domain.entities.XeMay;
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
public class ExportMotorbikesUseCaseControlTest {

    @Mock
    private MotorbikeRepository motorbikeRepository;

    @Mock
    private GenerateExcelFileInputBoundary generateExcelFileUseCase;

    @Mock
    private FormatDataForExportInputBoundary formatDataForExportUseCase;

    @Mock
    private ExportMotorbikesOutputBoundary outputBoundary;

    private ExportMotorbikesUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ExportMotorbikesUseCaseControl(
            outputBoundary,
            motorbikeRepository,
            formatDataForExportUseCase,
            generateExcelFileUseCase
        );
    }

    @Test
    void shouldExportMotorbikesSuccessfully() {
        // Given
        XeMay motorbike1 = new XeMay("Yamaha Exciter", "Xe", 
            java.math.BigDecimal.valueOf(45000000), "image1.jpg", 10,
            "Yamaha", "Exciter", "Đỏ", 2024, 150);
        motorbike1.setMaSP(1L);

        XeMay motorbike2 = new XeMay("Honda Wave", "Xe", 
            java.math.BigDecimal.valueOf(30000000), "image2.jpg", 15,
            "Honda", "Wave", "Xanh", 2024, 110);
        motorbike2.setMaSP(2L);

        List<XeMay> motorbikes = Arrays.asList(motorbike1, motorbike2);

        when(motorbikeRepository.findAllMotorbikes()).thenReturn(motorbikes);

        ExportMotorbikesInputData inputData = new ExportMotorbikesInputData();

        // When
        useCase.execute(inputData);

        // Then
        verify(motorbikeRepository).findAllMotorbikes();
        verify(formatDataForExportUseCase, atLeastOnce()).execute(any());
        verify(generateExcelFileUseCase, atLeastOnce()).execute(any());
    }

    @Test
    void shouldHandleEmptyMotorbikeList() {
        // Given
        List<XeMay> motorbikes = Arrays.asList();

        when(motorbikeRepository.findAllMotorbikes()).thenReturn(motorbikes);

        ExportMotorbikesInputData inputData = new ExportMotorbikesInputData();

        // When
        useCase.execute(inputData);

        // Then
        verify(motorbikeRepository).findAllMotorbikes();
    }
}
