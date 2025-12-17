package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;
import com.motorbike.business.dto.validateproductdata.ValidateProductDataOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateProductDataUseCaseControlTest {

    @Test
    void shouldValidateProductSuccessfully() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "Yamaha Exciter", "XE001", 45000000.0, 10, "xe_may"
        );
        ValidateProductDataUseCaseControl useCase = new ValidateProductDataUseCaseControl(null);

        // When
        ValidateProductDataOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertTrue(outputData.isValid());
        assertTrue(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenProductCodeIsEmpty() {
        // Given - productCode is now optional, empty string is valid
        // Test validates that when product code is empty, validation still passes if other fields are valid
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "Yamaha Exciter", "", 45000000.0, 10, "xe_may"
        );
        ValidateProductDataUseCaseControl useCase = new ValidateProductDataUseCaseControl(null);

        // When
        ValidateProductDataOutputData outputData = useCase.validateInternal(inputData);

        // Then - should be valid since productCode is optional
        assertTrue(outputData.isValid());
        assertTrue(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenProductNameIsEmpty() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "", "XE001", 45000000.0, 10, "xe_may"
        );
        ValidateProductDataUseCaseControl useCase = new ValidateProductDataUseCaseControl(null);

        // When
        ValidateProductDataOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenPriceIsNegative() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "Yamaha Exciter", "XE001", -1000.0, 10, "xe_may"
        );
        ValidateProductDataUseCaseControl useCase = new ValidateProductDataUseCaseControl(null);

        // When
        ValidateProductDataOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenStockIsNegative() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "Yamaha Exciter", "XE001", 45000000.0, -5, "xe_may"
        );
        ValidateProductDataUseCaseControl useCase = new ValidateProductDataUseCaseControl(null);

        // When
        ValidateProductDataOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }
}
