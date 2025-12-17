package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileInputData;
import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileOutputData;
import com.motorbike.business.usecase.output.GenerateExcelFileOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenerateExcelFileUseCaseControlTest {

    @Mock
    private GenerateExcelFileOutputBoundary outputBoundary;

    private GenerateExcelFileUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new GenerateExcelFileUseCaseControl(outputBoundary);
    }

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

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(GenerateExcelFileOutputData.class));
    }

    @Test
    void shouldGenerateExcelFileWithEmptyData() {
        // Given
        List<Map<String, Object>> data = Arrays.asList();
        List<String> headers = Arrays.asList("Mã SP", "Tên SP", "Giá");

        GenerateExcelFileInputData inputData = new GenerateExcelFileInputData(data, headers);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(GenerateExcelFileOutputData.class));
    }
}
