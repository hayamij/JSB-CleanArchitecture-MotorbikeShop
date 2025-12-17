package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileInputData;
import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileOutputData;
import com.motorbike.business.usecase.output.ValidateExcelFileOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateExcelFileUseCaseControlTest {

    @Mock
    private ValidateExcelFileOutputBoundary outputBoundary;

    private ValidateExcelFileUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateExcelFileUseCaseControl(outputBoundary);
    }

    @Test
    void shouldValidateExcelFileSuccessfully() {
        // Given
        MultipartFile file = new MockMultipartFile(
            "file", "products.xlsx", 
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
            "test data".getBytes()
        );

        ValidateExcelFileInputData inputData = new ValidateExcelFileInputData(file);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateExcelFileOutputData.class));
    }

    @Test
    void shouldFailWhenFileIsEmpty() {
        // Given
        MultipartFile file = new MockMultipartFile(
            "file", "products.xlsx", 
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
            new byte[0]
        );

        ValidateExcelFileInputData inputData = new ValidateExcelFileInputData(file);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateExcelFileOutputData.class));
    }

    @Test
    void shouldFailWhenFileIsNotExcel() {
        // Given
        MultipartFile file = new MockMultipartFile(
            "file", "products.txt", 
            "text/plain", 
            "test data".getBytes()
        );

        ValidateExcelFileInputData inputData = new ValidateExcelFileInputData(file);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateExcelFileOutputData.class));
    }
}
