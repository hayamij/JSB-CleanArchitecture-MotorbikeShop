package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.domain.entities.VaiTro;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginPresenter implements LoginOutputBoundary {
    
    private final LoginViewModel viewModel;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    
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

    
    private void presentSuccess(LoginOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        
        viewModel.userId = outputData.getUserId();
        viewModel.email = outputData.getEmail();
        viewModel.username = outputData.getUsername();
        viewModel.phone = outputData.getPhoneNumber();
        viewModel.address = outputData.getAddress();
        
        viewModel.role = outputData.getRole() != null ? outputData.getRole().name() : null;
        viewModel.roleDisplay = formatRoleDisplay(outputData.getRole());
        
        viewModel.lastLoginDisplay = formatDateTime(outputData.getLastLoginAt());
        
        viewModel.sessionToken = outputData.getSessionToken();
        
        viewModel.cartId = outputData.getCartId();
        
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
        
        viewModel.message = String.format(
            "Đăng nhập thành công! Xin chào %s (%s)",
            outputData.getUsername(),
            viewModel.roleDisplay
        );
        
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.errorColor = null;
    }

    
    private void presentError(LoginOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(
            outputData.getErrorCode(),
            outputData.getErrorMessage()
        );
        viewModel.errorColor = "RED";
        
        viewModel.message = "Đăng nhập thất bại";
        
        viewModel.userId = null;
        viewModel.email = null;
        viewModel.username = null;
        viewModel.roleDisplay = null;
        viewModel.lastLoginDisplay = null;
        viewModel.sessionToken = null;
        viewModel.cartMerged = false;
        viewModel.cartMergeMessage = null;
    }

    
    private String formatRoleDisplay(VaiTro role) {
        if (role == null) {
            return "không xác định";
        }
        return role.getMoTa();
    }

    
    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    
    private String formatErrorMessage(String errorCode, String errorMessage) {
        if (errorCode == null) {
            return errorMessage != null ? errorMessage : "Lỗi không xác định";
        }
        
        switch (errorCode) {
            case "USER_NOT_FOUND":
                return "Không tìm thấy tài khoản. Vui lòng kiểm tra lại email, tên đăng nhập hoặc số điện thoại.";
            
            case "EMAIL_NOT_FOUND":
                return "Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại hoặc đăng ký tài khoản mới.";
            
            case "WRONG_PASSWORD":
            case "INVALID_PASSWORD":
                return "Mật khẩu không chính xác. Vui lòng thử lại.";
            
            case "ACCOUNT_LOCKED":
            case "USER_INACTIVE":
                return "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên.";
            
            case "EMPTY_LOGIN_IDENTIFIER":
                return "Vui lòng nhập email, tên đăng nhập hoặc số điện thoại.";
            
            case "EMPTY_EMAIL":
                return "Vui lòng nhập địa chỉ email.";
            
            case "EMPTY_USERNAME":
                return "Vui lòng nhập tên đăng nhập.";
            
            case "EMPTY_PHONE":
                return "Vui lòng nhập số điện thoại.";
            
            case "INVALID_EMAIL":
            case "INVALID_EMAIL_FORMAT":
                return "Định dạng email không hợp lệ. Vui lòng nhập đúng định dạng (vd: user@example.com).";
            
            case "INVALID_PHONE_FORMAT":
                return "Số điện thoại không đúng định dạng (VD: 0912345678 hoặc +84912345678).";
            
            case "EMPTY_PASSWORD":
                return "Vui lòng nhập mật khẩu.";
            
            case "PASSWORD_TOO_SHORT":
                return "Mật khẩu phải có ít nhất 6 ký tự.";
            
            case "INVALID_INPUT":
            case "NULL_INPUT":
                return "Dữ liệu đầu vào không hợp lệ.";
            
            case "UNEXPECTED_ERROR":
                return "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau. Chi tiết: " + errorMessage;
            
            default:
                return errorMessage != null ? errorMessage : "Lỗi không xác định: " + errorCode;
        }
    }
}
