package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileInputData;
import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileOutputData;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateExcelFileUseCaseControlTest {

    @Test
    void shouldGenerateExcelFileSuccessfully() {
        // Given
        Map<String, Object> row1 = new HashMap<>();
        row1.put("Mã SP", "XE001");
        row1.put("Tên SP", "Yamaha Exciter");
        row1.put("Giá", 45000000.0);
        row1.put("Số lượng", 10);

        Map<String, Object> row2 = new HashMap<>();
        row2.put("Mã SP", "PT001");
        row2.put("Tên SP", "Mũ bảo hiểm");
        row2.put("Giá", 500000.0);
        row2.put("Số lượng", 20);

        List<Map<String, Object>> data = Arrays.asList(row1, row2);
        List<String> headers = Arrays.asList("Mã SP", "Tên SP", "Giá", "Số lượng");

        GenerateExcelFileInputData inputData = new GenerateExcelFileInputData(data, headers);
        GenerateExcelFileUseCaseControl useCase = new GenerateExcelFileUseCaseControl(null);

        // When
        GenerateExcelFileOutputData outputData = useCase.generateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotNull(outputData.getFileBytes());
        assertTrue(outputData.getFileBytes().length > 0);
        assertEquals(2, outputData.getRowCount());
        assertNotNull(outputData.getFileName());
    }

    @Test
    void shouldGenerateExcelFileWithEmptyData() {
        // Given
        List<Map<String, Object>> data = Collections.emptyList();
        List<String> headers = Arrays.asList("Mã SP", "Tên SP", "Giá");

        GenerateExcelFileInputData inputData = new GenerateExcelFileInputData(data, headers);
        GenerateExcelFileUseCaseControl useCase = new GenerateExcelFileUseCaseControl(null);

        // When
        GenerateExcelFileOutputData outputData = useCase.generateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotNull(outputData.getFileBytes());
        assertEquals(0, outputData.getRowCount());
    }

    @Test
    void shouldHandleNullData() {
        // Given
        GenerateExcelFileInputData inputData = new GenerateExcelFileInputData(null, Arrays.asList("Header"));
        GenerateExcelFileUseCaseControl useCase = new GenerateExcelFileUseCaseControl(null);

        // When
        GenerateExcelFileOutputData outputData = useCase.generateInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotNull(outputData.getErrorCode());
    }
}
