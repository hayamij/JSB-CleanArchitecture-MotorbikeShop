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
            "XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10
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
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10
        );
        ValidateProductDataUseCaseControl useCase = new ValidateProductDataUseCaseControl(null);

        // When
        ValidateProductDataOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenProductNameIsEmpty() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "XE001", "", "Xe thể thao", 45000000.0, 10
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
            "XE001", "Yamaha Exciter", "Xe thể thao", -1000.0, 10
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
            "XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, -5
        );
        ValidateProductDataUseCaseControl useCase = new ValidateProductDataUseCaseControl(null);

        // When
        ValidateProductDataOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }
}
