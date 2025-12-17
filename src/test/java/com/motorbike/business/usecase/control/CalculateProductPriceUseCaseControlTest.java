package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceInputData;
import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceOutputData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculateProductPriceUseCaseControlTest {

    private CalculateProductPriceUseCaseControl useCase = new CalculateProductPriceUseCaseControl(null);

    @Test
    void shouldCalculatePriceWithDiscount() {
        // Given
        double basePrice = 45000000.0;
        int discountPercent = 10;

        CalculateProductPriceInputData inputData = new CalculateProductPriceInputData(basePrice, discountPercent);

        // When
        CalculateProductPriceOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(0, new BigDecimal("45000000.0").compareTo(outputData.getBasePrice()));
        assertEquals(10, outputData.getDiscountPercent());
        assertEquals(0, new BigDecimal("4500000.00").compareTo(outputData.getDiscountAmount()));
        assertEquals(0, new BigDecimal("40500000.00").compareTo(outputData.getFinalPrice()));
    }

    @Test
    void shouldCalculatePriceWithZeroDiscount() {
        // Given
        double basePrice = 45000000.0;
        int discountPercent = 0;

        CalculateProductPriceInputData inputData = new CalculateProductPriceInputData(basePrice, discountPercent);

        // When
        CalculateProductPriceOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(0, new BigDecimal("45000000.0").compareTo(outputData.getBasePrice()));
        assertEquals(0, outputData.getDiscountPercent());
        assertEquals(0, new BigDecimal("0.00").compareTo(outputData.getDiscountAmount()));
        assertEquals(0, new BigDecimal("45000000.00").compareTo(outputData.getFinalPrice()));
    }

    @Test
    void shouldFailWithNegativePrice() {
        // Given
        double basePrice = -45000000.0;
        int discountPercent = 10;

        CalculateProductPriceInputData inputData = new CalculateProductPriceInputData(basePrice, discountPercent);

        // When
        CalculateProductPriceOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
    }

    @Test
    void shouldCalculatePriceWith50PercentDiscount() {
        // Given
        double basePrice = 1000000.0;
        int discountPercent = 50;

        CalculateProductPriceInputData inputData = new CalculateProductPriceInputData(basePrice, discountPercent);

        // When
        CalculateProductPriceOutputData outputData = useCase.calculateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(new BigDecimal("1000000.0"), outputData.getBasePrice());
        assertEquals(50, outputData.getDiscountPercent());
        assertEquals(new BigDecimal("500000.00"), outputData.getDiscountAmount());
        assertEquals(new BigDecimal("500000.00"), outputData.getFinalPrice());
    }
}
