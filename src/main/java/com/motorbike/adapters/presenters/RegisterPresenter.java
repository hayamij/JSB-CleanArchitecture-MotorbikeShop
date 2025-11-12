package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.RegisterViewModel;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.usecase.RegisterOutputBoundary;
import com.motorbike.domain.entities.VaiTro;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Presenter for Register Feature
 * Transforms RegisterOutputData → RegisterViewModel
 * Contains presentation logic (formatting, display rules)
 * NO business logic
 */
public class RegisterPresenter implements RegisterOutputBoundary {
    
    private final RegisterViewModel viewModel;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Constructor
     * @param viewModel View model to be updated
     */
    public RegisterPresenter(RegisterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(RegisterOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }

    /**
     * Present success case
     * @param outputData Output data from use case
     */
    private void presentSuccess(RegisterOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        
        // User information
        viewModel.userId = outputData.getUserId();
        viewModel.email = outputData.getEmail();
        viewModel.username = outputData.getUsername();
        
        // Format role display
        viewModel.roleDisplay = formatRoleDisplay(outputData.getRole());
        
        // Format registration time
        viewModel.registeredAtDisplay = formatDateTime(outputData.getCreatedAt());
        
        // Auto-login information
        viewModel.autoLoginEnabled = outputData.isAutoLoginEnabled();
        viewModel.sessionToken = outputData.getSessionToken();
        
        // Redirect URL (can be customized)
        if (viewModel.autoLoginEnabled) {
            viewModel.redirectUrl = "/home"; // Auto-login enabled, go to home
        } else {
            viewModel.redirectUrl = "/login"; // Need to login manually
        }
        
        // Success message
        viewModel.message = String.format(
            "Đăng ký tài khoản thành công! Chào mừng %s (%s)",
            outputData.getUsername(),
            viewModel.roleDisplay
        );
        
        // Clear error fields
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.errorColor = null;
        viewModel.emailError = null;
        viewModel.usernameError = null;
        viewModel.passwordError = null;
        viewModel.phoneError = null;
    }

    /**
     * Present error case
     * @param outputData Output data from use case
     */
    private void presentError(RegisterOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        
        // Error information
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(
            outputData.getErrorCode(), 
            outputData.getErrorMessage()
        );
        viewModel.errorColor = "RED";
        
        // Map error to specific field (for form validation display)
        mapErrorToField(outputData.getErrorCode());
        
        // Main message
        viewModel.message = "Đăng ký tài khoản thất bại";
        
        // Clear success fields
        viewModel.userId = null;
        viewModel.email = null;
        viewModel.username = null;
        viewModel.roleDisplay = null;
        viewModel.registeredAtDisplay = null;
        viewModel.autoLoginEnabled = false;
        viewModel.sessionToken = null;
        viewModel.redirectUrl = null;
    }

    /**
     * Map error code to specific field error
     * @param errorCode Error code from use case
     */
    private void mapErrorToField(String errorCode) {
        if (errorCode == null) return;
        
        switch (errorCode) {
            case "EMPTY_EMAIL":
            case "INVALID_EMAIL_FORMAT":
            case "EMAIL_ALREADY_EXISTS":
                viewModel.emailError = formatErrorMessage(errorCode, null);
                break;
            
            case "EMPTY_USERNAME":
            case "USERNAME_TOO_SHORT":
            case "USERNAME_TOO_LONG":
                viewModel.usernameError = formatErrorMessage(errorCode, null);
                break;
            
            case "EMPTY_PASSWORD":
            case "PASSWORD_TOO_SHORT":
            case "PASSWORD_MISMATCH":
            case "EMPTY_CONFIRM_PASSWORD":
                viewModel.passwordError = formatErrorMessage(errorCode, null);
                break;
            
            case "EMPTY_PHONE":
            case "INVALID_PHONE_FORMAT":
                viewModel.phoneError = formatErrorMessage(errorCode, null);
                break;
            
            default:
                // General error, not field-specific
                break;
        }
    }

    /**
     * Format user role for display
     * @param role User role enum
     * @return Formatted role string in Vietnamese
     */
    private String formatRoleDisplay(VaiTro role) {
        if (role == null) {
            return "không xác định";
        }
        return role.getMoTa();
    }

    /**
     * Format datetime for display
     * @param dateTime LocalDateTime to format
     * @return Formatted datetime string
     */
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Format error message with user-friendly text
     * @param errorCode Error code from use case
     * @param errorMessage Original error message
     * @return Formatted error message
     */
    private String formatErrorMessage(String errorCode, String errorMessage) {
        if (errorCode == null) {
            return errorMessage != null ? errorMessage : "Lỗi không xác định";
        }
        
        // Provide user-friendly error messages
        switch (errorCode) {
            case "EMAIL_ALREADY_EXISTS":
                return "Email này đã được đăng ký. Vui lòng sử dụng email khác hoặc đăng nhập.";
            
            case "PASSWORD_MISMATCH":
                return "Mật khẩu xác nhận không khớp. Vui lòng nhập lại.";
            
            case "EMPTY_EMAIL":
                return "Vui lòng nhập địa chỉ email.";
            
            case "INVALID_EMAIL_FORMAT":
                return "Định dạng email không hợp lệ. Vui lòng nhập đúng định dạng (vd: user@example.com).";
            
            case "EMPTY_USERNAME":
                return "Vui lòng nhập tên người dùng.";
            
            case "USERNAME_TOO_SHORT":
                return "Tên người dùng phải có ít nhất 3 ký tự.";
            
            case "USERNAME_TOO_LONG":
                return "Tên người dùng không được vượt quá 50 ký tự.";
            
            case "EMPTY_PASSWORD":
                return "Vui lòng nhập mật khẩu.";
            
            case "PASSWORD_TOO_SHORT":
                return "Mật khẩu phải có ít nhất 6 ký tự.";
            
            case "EMPTY_CONFIRM_PASSWORD":
                return "Vui lòng xác nhận mật khẩu.";
            
            case "EMPTY_PHONE":
                return "Vui lòng nhập số điện thoại.";
            
            case "INVALID_PHONE_FORMAT":
                return "Số điện thoại không hợp lệ. Vui lòng nhập đúng định dạng (VD: 0912345678).";
            
            case "NULL_INPUT":
                return "Dữ liệu đầu vào không hợp lệ.";
            
            case "UNEXPECTED_ERROR":
                return "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau. Chi tiết: " + errorMessage;
            
            default:
                return errorMessage != null ? errorMessage : "Lỗi không xác định: " + errorCode;
        }
    }
}
