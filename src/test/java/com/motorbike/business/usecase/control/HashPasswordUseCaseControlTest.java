package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.hashpassword.HashPasswordInputData;
import com.motorbike.business.dto.hashpassword.HashPasswordOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashPasswordUseCaseControlTest {

    @Test
    void shouldHashPasswordSuccessfully() {
        // Given
        String plainPassword = "SecurePass123";
        HashPasswordInputData inputData = new HashPasswordInputData(plainPassword);
        HashPasswordUseCaseControl useCase = new HashPasswordUseCaseControl(null);

        // When
        HashPasswordOutputData outputData = useCase.hashInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotNull(outputData.getHashedPassword());
        assertFalse(outputData.getHashedPassword().isEmpty());
        assertTrue(outputData.getHashedPassword().length() > 20); // Hashed passwords are longer
    }

    @Test
    void shouldProduceDifferentHashesForSamePassword() {
        // Given
        String plainPassword = "SecurePass123";
        HashPasswordInputData inputData = new HashPasswordInputData(plainPassword);
        HashPasswordUseCaseControl useCase = new HashPasswordUseCaseControl(null);

        // When
        HashPasswordOutputData outputData1 = useCase.hashInternal(inputData);
        HashPasswordOutputData outputData2 = useCase.hashInternal(inputData);

        // Then
        assertTrue(outputData1.isSuccess());
        assertTrue(outputData2.isSuccess());
        assertNotNull(outputData1.getHashedPassword());
        assertNotNull(outputData2.getHashedPassword());
        // Due to salt, hashes should be different
        assertNotEquals(outputData1.getHashedPassword(), outputData2.getHashedPassword());
    }

    @Test
    void shouldHandleNullPassword() {
        // Given
        HashPasswordInputData inputData = new HashPasswordInputData(null);
        HashPasswordUseCaseControl useCase = new HashPasswordUseCaseControl(null);

        // When
        HashPasswordOutputData outputData = useCase.hashInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotNull(outputData.getErrorCode());
    }

    @Test
    void shouldHandleEmptyPassword() {
        // Given
        HashPasswordInputData inputData = new HashPasswordInputData("");
        HashPasswordUseCaseControl useCase = new HashPasswordUseCaseControl(null);

        // When
        HashPasswordOutputData outputData = useCase.hashInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotNull(outputData.getErrorCode());
    }
}
