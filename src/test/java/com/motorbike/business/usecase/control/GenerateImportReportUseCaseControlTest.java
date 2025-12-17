package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.generateimportreport.GenerateImportReportInputData;
import com.motorbike.business.dto.generateimportreport.GenerateImportReportOutputData;
import com.motorbike.business.usecase.output.GenerateImportReportOutputBoundary;
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
public class GenerateImportReportUseCaseControlTest {

    @Mock
    private GenerateImportReportOutputBoundary outputBoundary;

    private GenerateImportReportUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new GenerateImportReportUseCaseControl(outputBoundary);
    }

    @Test
    void shouldGenerateReportSuccessfully() {
        // Given
        int totalRows = 12;
        int successCount = 10;
        List<String> errors = Arrays.asList("Row 3: Invalid price", "Row 7: Missing product code");

        GenerateImportReportInputData inputData = GenerateImportReportInputData.fromMessages(
            totalRows, successCount, errors
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(GenerateImportReportOutputData.class));
    }

    @Test
    void shouldGenerateReportWithNoErrors() {
        // Given
        int totalRows = 10;
        int successCount = 10;
        List<String> errors = Arrays.asList();

        GenerateImportReportInputData inputData = GenerateImportReportInputData.fromMessages(
            totalRows, successCount, errors
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(GenerateImportReportOutputData.class));
    }
}
