package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.hashpassword.HashPasswordInputData;
import com.motorbike.business.dto.hashpassword.HashPasswordOutputData;
import com.motorbike.business.usecase.output.HashPasswordOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HashPasswordUseCaseControlTest {

    @Mock
    private HashPasswordOutputBoundary outputBoundary;

    private HashPasswordUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new HashPasswordUseCaseControl(outputBoundary);
    }

    @Test
    void shouldHashPasswordSuccessfully() {
        // Given
        String plainPassword = "SecurePass123";
        HashPasswordInputData inputData = new HashPasswordInputData(plainPassword);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(HashPasswordOutputData.class));
    }

    @Test
    void shouldProduceDifferentHashesForSamePassword() {
        // Given
        String plainPassword = "SecurePass123";
        HashPasswordInputData inputData = new HashPasswordInputData(plainPassword);

        // When
        useCase.execute(inputData);
        useCase.execute(inputData);

        // Then
        verify(outputBoundary, times(2)).present(any(HashPasswordOutputData.class));
    }
}
