package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.verifypassword.VerifyPasswordInputData;
import com.motorbike.business.dto.verifypassword.VerifyPasswordOutputData;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class VerifyPasswordUseCaseControlTest {

    // VerifyPassword use case uses SHA-256 with salt (not BCrypt)
    
    private String hashPasswordWithSalt(String password) throws Exception {
        // Generate salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        // Hash password with salt
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        
        // Combine salt + hash and encode
        byte[] combined = new byte[salt.length + hashedPassword.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);
        
        return Base64.getEncoder().encodeToString(combined);
    }

    @Test
    void shouldVerifyCorrectPassword() throws Exception {
        // Given - Hash password using SHA-256 with salt
        String plainPassword = "SecurePass123";
        String hashedPassword = hashPasswordWithSalt(plainPassword);

        VerifyPasswordInputData inputData = new VerifyPasswordInputData(plainPassword, hashedPassword);
        VerifyPasswordUseCaseControl useCase = new VerifyPasswordUseCaseControl(null);

        // When
        VerifyPasswordOutputData outputData = useCase.verifyInternal(inputData);

        // Then
        assertTrue(outputData.isValid());
    }

    @Test
    void shouldRejectIncorrectPassword() throws Exception {
        // Given - Hash different password
        String plainPassword = "WrongPassword";
        String hashedPassword = hashPasswordWithSalt("SecurePass123");

        VerifyPasswordInputData inputData = new VerifyPasswordInputData(plainPassword, hashedPassword);
        VerifyPasswordUseCaseControl useCase = new VerifyPasswordUseCaseControl(null);

        // When
        VerifyPasswordOutputData outputData = useCase.verifyInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
    }
    
    @Test
    void shouldFailWhenPasswordIsNull() {
        // Given - Null plain password
        String hashedPassword = "someHash";
        
        VerifyPasswordInputData inputData = new VerifyPasswordInputData(null, hashedPassword);
        VerifyPasswordUseCaseControl useCase = new VerifyPasswordUseCaseControl(null);

        // When
        VerifyPasswordOutputData outputData = useCase.verifyInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
    }
}
