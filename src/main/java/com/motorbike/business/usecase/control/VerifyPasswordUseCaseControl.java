package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.verifypassword.VerifyPasswordInputData;
import com.motorbike.business.dto.verifypassword.VerifyPasswordOutputData;
import com.motorbike.business.usecase.input.VerifyPasswordInputBoundary;
import com.motorbike.business.usecase.output.VerifyPasswordOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class VerifyPasswordUseCaseControl implements VerifyPasswordInputBoundary {
    private final VerifyPasswordOutputBoundary outputBoundary;

    public VerifyPasswordUseCaseControl(VerifyPasswordOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(VerifyPasswordInputData inputData) {
        VerifyPasswordOutputData outputData = verifyInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    // Overload to accept DTO from user package
    public VerifyPasswordOutputData verifyInternal(com.motorbike.business.dto.user.VerifyPasswordInputData inputData) {
        // Convert to the expected DTO type
        VerifyPasswordInputData convertedInput = new VerifyPasswordInputData(inputData.getPlainPassword(), inputData.getHashedPassword());
        return verifyInternal(convertedInput);
    }

    public VerifyPasswordOutputData verifyInternal(VerifyPasswordInputData inputData) {
        VerifyPasswordOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("VerifyPassword");
            }
            if (inputData.getPlainPassword() == null || inputData.getPlainPassword().isEmpty()) {
                throw ValidationException.invalidInput("Plain password is required");
            }
            if (inputData.getHashedPassword() == null || inputData.getHashedPassword().isEmpty()) {
                throw ValidationException.invalidInput("Hashed password is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - verify password
        if (errorException == null) {
            try {
                String plainPassword = inputData.getPlainPassword();
                String hashedPassword = inputData.getHashedPassword();

                boolean isValid;
                
                // Check if the stored password is hashed (Base64 encoded with salt+hash structure) or plaintext
                try {
                    // Try to decode as Base64
                    byte[] combined = Base64.getDecoder().decode(hashedPassword);

                    // Check if it has the expected structure (at least 16 bytes for salt + 32 for SHA-256 hash)
                    if (combined.length < 48) {
                        throw new IllegalArgumentException("Not a valid hashed password");
                    }

                    // Extract salt (first 16 bytes)
                    byte[] salt = new byte[16];
                    System.arraycopy(combined, 0, salt, 0, 16);

                    // Extract hash (remaining bytes)
                    byte[] storedHash = new byte[combined.length - 16];
                    System.arraycopy(combined, 16, storedHash, 0, storedHash.length);

                    // Hash the input password with the extracted salt
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(salt);
                    byte[] computedHash = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

                    // Compare the hashes
                    isValid = Arrays.equals(storedHash, computedHash);
                } catch (IllegalArgumentException e) {
                    // Not properly hashed (not Base64 or wrong structure), assume plaintext password (for testing/legacy)
                    isValid = plainPassword.equals(hashedPassword);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("SHA-256 algorithm not available", e);
                }

                outputData = new VerifyPasswordOutputData(isValid);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "VERIFY_ERROR";
            outputData = VerifyPasswordOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}
