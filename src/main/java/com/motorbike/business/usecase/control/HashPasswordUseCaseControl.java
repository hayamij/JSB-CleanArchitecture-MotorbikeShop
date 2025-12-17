package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.hashpassword.HashPasswordInputData;
import com.motorbike.business.dto.hashpassword.HashPasswordOutputData;
import com.motorbike.business.usecase.input.HashPasswordInputBoundary;
import com.motorbike.business.usecase.output.HashPasswordOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashPasswordUseCaseControl implements HashPasswordInputBoundary {
    private final HashPasswordOutputBoundary outputBoundary;

    public HashPasswordUseCaseControl(HashPasswordOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(HashPasswordInputData inputData) {
        HashPasswordOutputData outputData = hashInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    // Overload to accept DTO from user package
    public HashPasswordOutputData hashInternal(com.motorbike.business.dto.user.HashPasswordInputData inputData) {
        // Convert to the expected DTO type
        HashPasswordInputData convertedInput = new HashPasswordInputData(inputData.getPlainPassword());
        return hashInternal(convertedInput);
    }

    public HashPasswordOutputData hashInternal(HashPasswordInputData inputData) {
        HashPasswordOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("HashPassword");
            }
            if (inputData.getPlainPassword() == null || inputData.getPlainPassword().isEmpty()) {
                throw ValidationException.invalidInput("Password is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - hash password with salt
        if (errorException == null) {
            try {
                String plainPassword = inputData.getPlainPassword();
                
                // Generate random salt
                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[16];
                random.nextBytes(salt);

                // Hash password with salt using SHA-256
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(salt);
                byte[] hashedBytes = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

                // Combine salt and hash, then encode to Base64
                byte[] combined = new byte[salt.length + hashedBytes.length];
                System.arraycopy(salt, 0, combined, 0, salt.length);
                System.arraycopy(hashedBytes, 0, combined, salt.length, hashedBytes.length);

                String hashedPassword = Base64.getEncoder().encodeToString(combined);

                outputData = new HashPasswordOutputData(hashedPassword);

            } catch (NoSuchAlgorithmException e) {
                errorException = new RuntimeException("SHA-256 algorithm not available", e);
            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "HASH_ERROR";
            outputData = HashPasswordOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}
