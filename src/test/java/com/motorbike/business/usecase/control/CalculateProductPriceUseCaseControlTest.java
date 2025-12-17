package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceInputData;
import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceOutputData;
import com.motorbike.business.usecase.output.CalculateProductPriceOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculateProductPriceUseCaseControlTest {

    @Mock
    private CalculateProductPriceOutputBoundary outputBoundary;

    private CalculateProductPriceUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CalculateProductPriceUseCaseControl(outputBoundary);
    }

    @Test
    void shouldCalculatePriceSuccessfully() {
        // Given
        double basePrice = 45000000.0;
        int quantity = 5;

        CalculateProductPriceInputData inputData = new CalculateProductPriceInputData(basePrice, quantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CalculateProductPriceOutputData.class));
    }

    @Test
    void shouldHandleZeroQuantity() {
        // Given
        double basePrice = 45000000.0;
        int quantity = 0;

        CalculateProductPriceInputData inputData = new CalculateProductPriceInputData(basePrice, quantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CalculateProductPriceOutputData.class));
    }
}
