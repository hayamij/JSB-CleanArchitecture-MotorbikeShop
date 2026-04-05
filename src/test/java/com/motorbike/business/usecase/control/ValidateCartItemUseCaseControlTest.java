package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validatecartitem.ValidateCartItemInputData;
import com.motorbike.business.dto.validatecartitem.ValidateCartItemOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateCartItemUseCaseControlTest {

    // ValidateCartItem use case ONLY validates input data (productId, quantity)
    // It does NOT access repository or check product stock/visibility

    @Test
    void shouldValidateCartItemSuccessfully() {
        // Given - Valid product ID and quantity
        Long productId = 1L;
        int quantity = 5;
        ValidateCartItemUseCaseControl useCase = new ValidateCartItemUseCaseControl(null, null);
        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, quantity);

        // When
        ValidateCartItemOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertTrue(outputData.isValid());
        assertEquals("Cart item is valid", outputData.getMessage());
        assertEquals(productId, outputData.getProductId());
    }

    @Test
    void shouldFailWhenProductIdIsNull() {
        // Given - Null product ID
        ValidateCartItemUseCaseControl useCase = new ValidateCartItemUseCaseControl(null, null);
        ValidateCartItemInputData inputData = new ValidateCartItemInputData(null, 5);

        // When
        ValidateCartItemOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getMessage());
    }

    @Test
    void shouldFailWhenQuantityIsZero() {
        // Given - Quantity is zero
        Long productId = 1L;
        ValidateCartItemUseCaseControl useCase = new ValidateCartItemUseCaseControl(null, null);
        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, 0);

        // When
        ValidateCartItemOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getMessage());
    }

    @Test
    void shouldFailWhenQuantityIsNegative() {
        // Given - Negative quantity
        Long productId = 1L;
        ValidateCartItemUseCaseControl useCase = new ValidateCartItemUseCaseControl(null, null);
        ValidateCartItemInputData inputData = new ValidateCartItemInputData(productId, -5);

        // When
        ValidateCartItemOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getMessage());
    }

    @Test
    void shouldFailWhenInputIsNull() {
        // Given - Null input data
        ValidateCartItemUseCaseControl useCase = new ValidateCartItemUseCaseControl(null, null);

        // When
        ValidateCartItemOutputData outputData = useCase.validateInternal(null);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getMessage());
    }
}
