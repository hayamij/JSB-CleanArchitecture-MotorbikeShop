package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateimportrow.ValidateImportRowInputData;
import com.motorbike.business.dto.validateimportrow.ValidateImportRowOutputData;
import com.motorbike.business.usecase.output.ValidateImportRowOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateImportRowUseCaseControlTest {

    @Mock
    private ValidateImportRowOutputBoundary outputBoundary;

    private ValidateImportRowUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateImportRowUseCaseControl(outputBoundary);
    }

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

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateImportRowOutputData.class));
    }

    @Test
    void shouldFailWhenRequiredFieldIsMissing() {
        // Given
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("Mã SP", "XE001");
        // Missing other required fields

        ValidateImportRowInputData inputData = new ValidateImportRowInputData(rowData, 1);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateImportRowOutputData.class));
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

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateImportRowOutputData.class));
    }
}
