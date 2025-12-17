package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.verifypassword.VerifyPasswordInputData;
import com.motorbike.business.dto.verifypassword.VerifyPasswordOutputData;
import com.motorbike.business.usecase.output.VerifyPasswordOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerifyPasswordUseCaseControlTest {

    @Mock
    private VerifyPasswordOutputBoundary outputBoundary;

    private VerifyPasswordUseCaseControl useCase;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        useCase = new VerifyPasswordUseCaseControl(outputBoundary);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void shouldVerifyCorrectPassword() {
        // Given
        String plainPassword = "SecurePass123";
        String hashedPassword = passwordEncoder.encode(plainPassword);

        VerifyPasswordInputData inputData = new VerifyPasswordInputData(plainPassword, hashedPassword);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(VerifyPasswordOutputData.class));
    }

    @Test
    void shouldRejectIncorrectPassword() {
        // Given
        String plainPassword = "WrongPassword";
        String hashedPassword = passwordEncoder.encode("SecurePass123");

        VerifyPasswordInputData inputData = new VerifyPasswordInputData(plainPassword, hashedPassword);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(VerifyPasswordOutputData.class));
    }
}
