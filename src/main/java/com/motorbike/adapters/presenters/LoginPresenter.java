package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.domain.entities.VaiTro;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Presenter for Login Feature
 * Transforms LoginOutputData → LoginViewModel
 * Contains presentation logic (formatting, display rules)
 * NO business logic
 */
public class LoginPresenter implements LoginOutputBoundary {
    
    private final LoginViewModel viewModel;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Constructor
     * @param viewModel View model to be updated
     */
    public LoginPresenter(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(LoginOutputData outputData) {
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
    private void presentSuccess(LoginOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        
        // User information
        viewModel.userId = outputData.getUserId();
        viewModel.email = outputData.getEmail();
        viewModel.username = outputData.getUsername();
        
        // Format role display
        viewModel.roleDisplay = formatRoleDisplay(outputData.getRole());
        
        // Format last login time
        viewModel.lastLoginDisplay = formatDateTime(outputData.getLastLoginAt());
        
        // Session token
        viewModel.sessionToken = outputData.getSessionToken();
        
        // Cart ID
        viewModel.cartId = outputData.getCartId();
        
        // Cart merge information
        viewModel.cartMerged = outputData.isCartMerged();
        viewModel.mergedItemsCount = outputData.getMergedItemsCount();
        if (outputData.isCartMerged()) {
            viewModel.cartMergeMessage = String.format(
                "Đã thêm %d sản phẩm từ giỏ hàng tạm vào giỏ hàng của bạn",
                outputData.getMergedItemsCount()
            );
        } else {
            viewModel.cartMergeMessage = null;
        }
        
        // Success message
        viewModel.message = String.format(
            "Đăng nhập thành công! Xin chào %s (%s)",
            outputData.getUsername(),
            viewModel.roleDisplay
        );
        
        // Clear error fields
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.errorColor = null;
    }

    /**
     * Present error case
     * @param outputData Output data from use case
     */
    private void presentError(LoginOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        
        // Error information
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(
            outputData.getErrorCode(), 
            outputData.getErrorMessage()
        );
        viewModel.errorColor = "RED";
        
        // Main message
        viewModel.message = "Đăng nhập thất bại";
        
        // Clear success fields
        viewModel.userId = null;
        viewModel.email = null;
        viewModel.username = null;
        viewModel.roleDisplay = null;
        viewModel.lastLoginDisplay = null;
        viewModel.sessionToken = null;
        viewModel.cartMerged = false;
        viewModel.cartMergeMessage = null;
    }

    /**
     * format role để hiển thị
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
            case "EMAIL_NOT_FOUND":
                return "Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại hoặc đăng ký tài khoản mới.";
            
            case "INVALID_PASSWORD":
                return "Mật khẩu không chính xác. Vui lòng thử lại.";
            
            case "USER_INACTIVE":
                return "Tài khoản đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.";
            
            case "EMPTY_EMAIL":
                return "Vui lòng nhập địa chỉ email.";
            
            case "INVALID_EMAIL_FORMAT":
                return "Định dạng email không hợp lệ. Vui lòng nhập đúng định dạng (vd: user@example.com).";
            
            case "EMPTY_PASSWORD":
                return "Vui lòng nhập mật khẩu.";
            
            case "PASSWORD_TOO_SHORT":
                return "Mật khẩu phải có ít nhất 6 ký tự.";
            
            case "NULL_INPUT":
                return "Dữ liệu đầu vào không hợp lệ.";
            
            case "UNEXPECTED_ERROR":
                return "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau. Chi tiết: " + errorMessage;
            
            default:
                return errorMessage != null ? errorMessage : "Lỗi không xác định: " + errorCode;
        }
    }
}
