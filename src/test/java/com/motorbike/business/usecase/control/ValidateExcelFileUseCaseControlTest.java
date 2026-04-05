package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileInputData;
import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileOutputData;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateExcelFileUseCaseControlTest {

    @Test
    void shouldValidateExcelFileSuccessfully() {
        // Given
        MultipartFile file = new MockMultipartFile(
            "file", "products.xlsx", 
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
            "test data".getBytes()
        );

        ValidateExcelFileInputData inputData = new ValidateExcelFileInputData(file);
        ValidateExcelFileUseCaseControl useCase = new ValidateExcelFileUseCaseControl(null);

        // When
        ValidateExcelFileOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertTrue(outputData.isValid());
        assertNull(outputData.getErrorMessage());
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
        ValidateExcelFileUseCaseControl useCase = new ValidateExcelFileUseCaseControl(null);

        // When
        ValidateExcelFileOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getErrorMessage());
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
        ValidateExcelFileUseCaseControl useCase = new ValidateExcelFileUseCaseControl(null);

        // When
        ValidateExcelFileOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getErrorMessage());
    }

    @Test
    void shouldHandleNullFile() {
        // Given
        ValidateExcelFileInputData inputData = new ValidateExcelFileInputData(null);
        ValidateExcelFileUseCaseControl useCase = new ValidateExcelFileUseCaseControl(null);

        // When
        ValidateExcelFileOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getErrorMessage());
    }
}
