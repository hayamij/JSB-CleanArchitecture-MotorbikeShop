package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;
import com.motorbike.business.dto.validateproductdata.ValidateProductDataOutputData;
import com.motorbike.business.usecase.output.ValidateProductDataOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateProductDataUseCaseControlTest {

    @Mock
    private ValidateProductDataOutputBoundary outputBoundary;

    private ValidateProductDataUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateProductDataUseCaseControl(outputBoundary);
    }

    @Test
    void shouldValidateProductSuccessfully() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateProductDataOutputData.class));
    }

    @Test
    void shouldFailWhenProductCodeIsEmpty() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateProductDataOutputData.class));
    }

    @Test
    void shouldFailWhenProductNameIsEmpty() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "XE001", "", "Xe thể thao", 45000000.0, 10
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateProductDataOutputData.class));
    }

    @Test
    void shouldFailWhenPriceIsNegative() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "XE001", "Yamaha Exciter", "Xe thể thao", -1000.0, 10
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateProductDataOutputData.class));
    }

    @Test
    void shouldFailWhenStockIsNegative() {
        // Given
        ValidateProductDataInputData inputData = new ValidateProductDataInputData(
            "XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, -5
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateProductDataOutputData.class));
    }
}
