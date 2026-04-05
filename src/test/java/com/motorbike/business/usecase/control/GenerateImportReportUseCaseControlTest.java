package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.generateimportreport.GenerateImportReportInputData;
import com.motorbike.business.dto.generateimportreport.GenerateImportReportOutputData;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateImportReportUseCaseControlTest {

    @Test
    void shouldGenerateReportSuccessfully() {
        // Given
        int totalRows = 12;
        int successCount = 10;
        List<String> errors = Arrays.asList("Row 3: Invalid price", "Row 7: Missing product code");

        GenerateImportReportInputData inputData = GenerateImportReportInputData.fromMessages(
            totalRows, successCount, errors
        );
        GenerateImportReportUseCaseControl useCase = new GenerateImportReportUseCaseControl(null);

        // When
        GenerateImportReportOutputData outputData = useCase.generateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(12, outputData.getTotalRows());
        assertEquals(10, outputData.getSuccessCount());
        assertEquals(2, outputData.getFailureCount());
        assertNotNull(outputData.getReport());
    }

    @Test
    void shouldGenerateReportWithNoErrors() {
        // Given
        int totalRows = 10;
        int successCount = 10;
        List<String> errors = Collections.emptyList();

        GenerateImportReportInputData inputData = GenerateImportReportInputData.fromMessages(
            totalRows, successCount, errors
        );
        GenerateImportReportUseCaseControl useCase = new GenerateImportReportUseCaseControl(null);

        // When
        GenerateImportReportOutputData outputData = useCase.generateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(10, outputData.getTotalRows());
        assertEquals(10, outputData.getSuccessCount());
        assertEquals(0, outputData.getFailureCount());
        assertNotNull(outputData.getReport());
    }

    @Test
    void shouldCalculateSuccessRate() {
        // Given
        int totalRows = 20;
        int successCount = 15;
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3", "Error 4", "Error 5");

        GenerateImportReportInputData inputData = GenerateImportReportInputData.fromMessages(
            totalRows, successCount, errors
        );
        GenerateImportReportUseCaseControl useCase = new GenerateImportReportUseCaseControl(null);

        // When
        GenerateImportReportOutputData outputData = useCase.generateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(5, outputData.getFailureCount());
        assertNotNull(outputData.getReport());
    }
}
