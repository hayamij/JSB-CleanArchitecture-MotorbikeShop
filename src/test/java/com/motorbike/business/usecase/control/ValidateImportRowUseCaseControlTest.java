package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateimportrow.ValidateImportRowInputData;
import com.motorbike.business.dto.validateimportrow.ValidateImportRowOutputData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateImportRowUseCaseControlTest {

    @Test
    void shouldValidateRowSuccessfully() {
        // Given
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Mã SP", "XE001");
        rowData.put("Tên SP", "Yamaha Exciter");
        rowData.put("Mô tả", "Xe thể thao");
        rowData.put("Giá", 45000000.0);
        rowData.put("Số lượng", 10);

        ValidateImportRowInputData inputData = new ValidateImportRowInputData(rowData, 1);
        ValidateImportRowUseCaseControl useCase = new ValidateImportRowUseCaseControl(null);

        // When
        ValidateImportRowOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertTrue(outputData.isValid());
        assertTrue(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenRequiredFieldIsMissing() {
        // Given
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Mã SP", "XE001");
        // Missing other required fields

        ValidateImportRowInputData inputData = new ValidateImportRowInputData(rowData, 1);
        ValidateImportRowUseCaseControl useCase = new ValidateImportRowUseCaseControl(null);

        // When
        ValidateImportRowOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenPriceIsNegative() {
        // Given
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Mã SP", "XE001");
        rowData.put("Tên SP", "Yamaha Exciter");
        rowData.put("Mô tả", "Xe thể thao");
        rowData.put("Giá", -1000.0);
        rowData.put("Số lượng", 10);

        ValidateImportRowInputData inputData = new ValidateImportRowInputData(rowData, 1);
        ValidateImportRowUseCaseControl useCase = new ValidateImportRowUseCaseControl(null);

        // When
        ValidateImportRowOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }
}
