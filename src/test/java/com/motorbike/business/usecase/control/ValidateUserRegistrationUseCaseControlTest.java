package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationInputData;
import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateUserRegistrationUseCaseControlTest {

    @Test
    void shouldValidateUserSuccessfully() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "john@example.com", "SecurePass123", "0123456789", "123 Main St"
        );
        ValidateUserRegistrationUseCaseControl useCase = new ValidateUserRegistrationUseCaseControl(null);

        // When
        ValidateUserRegistrationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertTrue(outputData.isValid());
        assertTrue(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenUsernameIsEmpty() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "", "john@example.com", "SecurePass123", "0123456789", "123 Main St"
        );
        ValidateUserRegistrationUseCaseControl useCase = new ValidateUserRegistrationUseCaseControl(null);

        // When
        ValidateUserRegistrationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "invalid-email", "SecurePass123", "0123456789", "123 Main St"
        );
        ValidateUserRegistrationUseCaseControl useCase = new ValidateUserRegistrationUseCaseControl(null);

        // When
        ValidateUserRegistrationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "john@example.com", "123", "0123456789", "123 Main St"
        );
        ValidateUserRegistrationUseCaseControl useCase = new ValidateUserRegistrationUseCaseControl(null);

        // When
        ValidateUserRegistrationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }

    @Test
    void shouldFailWhenPhoneIsInvalid() {
        // Given
        ValidateUserRegistrationInputData inputData = new ValidateUserRegistrationInputData(
            "john_doe", "john@example.com", "SecurePass123", "12345", "123 Main St"
        );
        ValidateUserRegistrationUseCaseControl useCase = new ValidateUserRegistrationUseCaseControl(null);

        // When
        ValidateUserRegistrationOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertFalse(outputData.getErrors().isEmpty());
    }
}
