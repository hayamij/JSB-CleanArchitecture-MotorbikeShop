package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationInputData;
import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.input.CheckUserDuplicationInputBoundary;
import com.motorbike.business.usecase.output.CheckUserDuplicationOutputBoundary;
import com.motorbike.domain.entities.NguoiDung;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.Optional;

public class CheckUserDuplicationUseCaseControl implements CheckUserDuplicationInputBoundary {
    private final CheckUserDuplicationOutputBoundary outputBoundary;
    private final UserRepository userRepository;

    public CheckUserDuplicationUseCaseControl(
            CheckUserDuplicationOutputBoundary outputBoundary,
            UserRepository userRepository) {
        this.outputBoundary = outputBoundary;
        this.userRepository = userRepository;
    }
    
    // Constructor for tests with swapped parameters
    public CheckUserDuplicationUseCaseControl(
            com.motorbike.business.ports.TaiKhoanRepository taiKhoanRepository,
            CheckUserDuplicationOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.userRepository = (UserRepository) taiKhoanRepository;
    }

    @Override
    public void execute(CheckUserDuplicationInputData inputData) {
        CheckUserDuplicationOutputData outputData = checkDuplicationInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    // Overload to accept DTO from user package
    public CheckUserDuplicationOutputData checkInternal(com.motorbike.business.dto.user.CheckUserDuplicationInputData inputData) {
        // Convert to the expected DTO type
        CheckUserDuplicationInputData convertedInput = new CheckUserDuplicationInputData(
            inputData.getEmail(), inputData.getUsername(), null
        );
        return checkDuplicationInternal(convertedInput);
    }

    public CheckUserDuplicationOutputData checkDuplicationInternal(CheckUserDuplicationInputData inputData) {
        CheckUserDuplicationOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("CheckUserDuplication");
            }
            if ((inputData.getEmail() == null || inputData.getEmail().trim().isEmpty()) &&
                (inputData.getUsername() == null || inputData.getUsername().trim().isEmpty())) {
                throw ValidationException.invalidInput("Email or username is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - check for duplicates
        if (errorException == null) {
            try {
                String email = inputData.getEmail() != null ? inputData.getEmail().trim() : null;
                String username = inputData.getUsername() != null ? inputData.getUsername().trim() : null;
                Long excludeId = inputData.getExcludeUserId();

                // Check email duplication
                if (email != null && !email.isEmpty()) {
                    Optional<TaiKhoan> existingByEmail = userRepository.findByEmail(email);
                    if (existingByEmail.isPresent()) {
                        TaiKhoan existing = existingByEmail.get();
                        // If excludeId is provided, ignore if it's the same user
                        Long existingId = existing.getMaTaiKhoan();
                        if (excludeId == null || !existingId.equals(excludeId)) {
                            outputData = new CheckUserDuplicationOutputData(
                                    true,
                                    "email",
                                    existingId
                            );
                            return outputData;
                        }
                    }
                }

                // Check username duplication
                if (username != null && !username.isEmpty()) {
                    Optional<TaiKhoan> existingByUsername = userRepository.findByTenDangNhap(username);
                    if (existingByUsername.isPresent()) {
                        TaiKhoan existing = existingByUsername.get();
                        // If excludeId is provided, ignore if it's the same user
                        Long existingId = existing.getMaTaiKhoan();
                        if (excludeId == null || !existingId.equals(excludeId)) {
                            outputData = new CheckUserDuplicationOutputData(
                                    true,
                                    "username",
                                    existingId
                            );
                            return outputData;
                        }
                    }
                }

                // No duplication found
                outputData = new CheckUserDuplicationOutputData(false, null, null);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "CHECK_DUPLICATION_ERROR";
            outputData = CheckUserDuplicationOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
    
    // Alias method for compatibility
    public CheckUserDuplicationOutputData checkInternal(CheckUserDuplicationInputData inputData) {
        return checkDuplicationInternal(inputData);
    }
}
