package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationInputData;
import com.motorbike.business.dto.validateuserregistration.ValidateUserRegistrationOutputData;
import com.motorbike.business.usecase.input.ValidateUserRegistrationInputBoundary;
import com.motorbike.business.usecase.output.ValidateUserRegistrationOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidateUserRegistrationUseCaseControl implements ValidateUserRegistrationInputBoundary {
    private final ValidateUserRegistrationOutputBoundary outputBoundary;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(\\+84|0)[0-9]{9,10}$"
    );

    public ValidateUserRegistrationUseCaseControl(ValidateUserRegistrationOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ValidateUserRegistrationInputData inputData) {
        ValidateUserRegistrationOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    // Overload to accept DTO from user package
    public ValidateUserRegistrationOutputData validateInternal(com.motorbike.business.dto.user.ValidateUserRegistrationInputData inputData) {
        // Convert to the expected DTO type
        ValidateUserRegistrationInputData convertedInput = new ValidateUserRegistrationInputData(
            inputData.getTenDangNhap(), inputData.getEmail(), inputData.getMatKhau(), 
            inputData.getHoTen(), inputData.getSoDienThoai()
        );
        return validateInternal(convertedInput);
    }

    public ValidateUserRegistrationOutputData validateInternal(ValidateUserRegistrationInputData inputData) {
        ValidateUserRegistrationOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ValidateUserRegistration");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - validate user registration data
        if (errorException == null) {
            try {
                List<String> errors = new ArrayList<>();

                // Validate email
                if (inputData.getEmail() == null || inputData.getEmail().trim().isEmpty()) {
                    errors.add("Email không được để trống");
                } else if (!EMAIL_PATTERN.matcher(inputData.getEmail().trim()).matches()) {
                    errors.add("Email không hợp lệ");
                } else if (inputData.getEmail().trim().length() > 100) {
                    errors.add("Email không được vượt quá 100 ký tự");
                }

                // Validate username
                if (inputData.getUsername() == null || inputData.getUsername().trim().isEmpty()) {
                    errors.add("Tên đăng nhập không được để trống");
                } else if (inputData.getUsername().trim().length() < 3) {
                    errors.add("Tên đăng nhập phải có ít nhất 3 ký tự");
                } else if (inputData.getUsername().trim().length() > 50) {
                    errors.add("Tên đăng nhập không được vượt quá 50 ký tự");
                } else if (!inputData.getUsername().matches("^[a-zA-Z0-9_]+$")) {
                    errors.add("Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới");
                }

                // Validate password
                if (inputData.getPassword() == null || inputData.getPassword().isEmpty()) {
                    errors.add("Mật khẩu không được để trống");
                } else if (inputData.getPassword().length() < 6) {
                    errors.add("Mật khẩu phải có ít nhất 6 ký tự");
                } else if (inputData.getPassword().length() > 100) {
                    errors.add("Mật khẩu không được vượt quá 100 ký tự");
                }

                // Validate phone number
                if (inputData.getPhoneNumber() != null && !inputData.getPhoneNumber().trim().isEmpty()) {
                    if (!PHONE_PATTERN.matcher(inputData.getPhoneNumber().trim()).matches()) {
                        errors.add("Số điện thoại không hợp lệ (phải bắt đầu bằng +84 hoặc 0 và có 10-11 số)");
                    }
                }

                // Validate full name
                if (inputData.getFullName() == null || inputData.getFullName().trim().isEmpty()) {
                    errors.add("Họ tên không được để trống");
                } else if (inputData.getFullName().trim().length() < 2) {
                    errors.add("Họ tên phải có ít nhất 2 ký tự");
                } else if (inputData.getFullName().trim().length() > 100) {
                    errors.add("Họ tên không được vượt quá 100 ký tự");
                }

                boolean isValid = errors.isEmpty();
                outputData = new ValidateUserRegistrationOutputData(isValid, errors);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "VALIDATION_ERROR";
            outputData = ValidateUserRegistrationOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}
