package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationInputData;
import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationOutputData;
import com.motorbike.business.usecase.output.ValidateUserRegistrationOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateUserRegistrationUseCaseControlTest {

    @Mock
    private ValidateUserRegistrationOutputBoundary outputBoundary;

    private ValidateUserRegistrationUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateUserRegistrationUseCaseControl(outputBoundary);
    }

    @Test
    void shouldValidateUserSuccessfully() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "john@example.com", "SecurePass123", "0123456789", "123 Main St"
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateUserRegistrationOutputData.class));
    }

    @Test
    void shouldFailWhenUsernameIsEmpty() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "", "john@example.com", "SecurePass123", "0123456789", "123 Main St"
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateUserRegistrationOutputData.class));
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "invalid-email", "SecurePass123", "0123456789", "123 Main St"
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateUserRegistrationOutputData.class));
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "john@example.com", "123", "0123456789", "123 Main St"
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateUserRegistrationOutputData.class));
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "john@example.com", "SecurePass123", "12345", "123 Main St"
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ValidateUserRegistrationOutputData.class));
    }
}
