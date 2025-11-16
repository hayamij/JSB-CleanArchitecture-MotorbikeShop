package com.motorbike.adapters.presenters;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;

/**
 * Presenter for Cancel Order Feature
 * Transforms CancelOrderOutputData → CancelOrderViewModel
 * Contains presentation logic (formatting, display rules)
 * NO business logic
 */
public class CancelOrderPresenter implements CancelOrderOutputBoundary {
    
    private final CancelOrderViewModel viewModel;
    private static final NumberFormat VND_FORMAT = 
        NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Constructor
     * @param viewModel View model to be updated
     */
    public CancelOrderPresenter(CancelOrderViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(CancelOrderOutputData outputData) {
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
    private void presentSuccess(CancelOrderOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        
        // Order information
        viewModel.orderId = outputData.getOrderId();
        viewModel.customerId = outputData.getCustomerId();
        viewModel.orderStatus = formatOrderStatus(outputData.getOrderStatus());
        viewModel.formattedRefundAmount = formatCurrency(outputData.getRefundAmount());
        viewModel.cancelReason = outputData.getCancelReason();
        viewModel.cancelledAtDisplay = formatDateTime(outputData.getCancelledAt());
        
        // Success message
        viewModel.message = String.format(
            "Hủy đơn hàng thành công! Mã đơn: #%d. Hoàn tiền: %s",
            outputData.getOrderId(),
            viewModel.formattedRefundAmount
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
    private void presentError(CancelOrderOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        
        // Error information
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(
            outputData.getErrorCode(), 
            outputData.getErrorMessage()
        );
        viewModel.errorColor = determineErrorColor(outputData.getErrorCode());
        
        // Main message
        viewModel.message = "Hủy đơn hàng thất bại";
        
        // Clear success fields
        viewModel.orderId = null;
        viewModel.customerId = null;
        viewModel.orderStatus = null;
        viewModel.formattedRefundAmount = null;
        viewModel.cancelReason = null;
        viewModel.cancelledAtDisplay = null;
    }

    /**
     * Format currency to VND
     * @param amount Amount to format
     * @return Formatted currency string
     */
    private String formatCurrency(java.math.BigDecimal amount) {
        if (amount == null) {
            return "₫0";
        }
        return VND_FORMAT.format(amount);
    }

    /**
     * Format order status
     * @param status Order status
     * @return Formatted status string
     */
    private String formatOrderStatus(String status) {
        if (status == null) {
            return "Không xác định";
        }
        
        switch (status) {
            case "CHO_XAC_NHAN":
                return "Chờ xác nhận";
            case "DA_XAC_NHAN":
                return "Đã xác nhận";
            case "DANG_GIAO":
                return "Đang giao hàng";
            case "DA_GIAO":
                return "Đã giao hàng";
            case "DA_HUY":
                return "Đã hủy";
            default:
                return status;
        }
    }

    /**
     * Format datetime for display
     * @param dateTime LocalDateTime to format
     * @return Formatted datetime string
     */
    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Determine error color based on error type
     * @param errorCode Error code
     * @return Color for UI
     */
    private String determineErrorColor(String errorCode) {
        if (errorCode == null) {
            return "RED";
        }
        
        switch (errorCode) {
            case "INVALID_ORDER_STATUS":
            case "CANNOT_CANCEL_ORDER":
                return "ORANGE";  // Warning
            case "PERMISSION_DENIED":
            case "ORDER_NOT_FOUND":
                return "RED";     // Error
            default:
                return "RED";
        }
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
        
        switch (errorCode) {
            case "ORDER_NOT_FOUND":
                return "Không tìm thấy đơn hàng. Vui lòng kiểm tra lại mã đơn.";
            
            case "PERMISSION_DENIED":
                return "Bạn không có quyền hủy đơn hàng này.";
            
            case "INVALID_ORDER_STATUS":
                return errorMessage;
            
            case "PRODUCT_NOT_FOUND":
                return "Lỗi: Sản phẩm trong đơn hàng không tồn tại.";
            
            case "INVALID_INPUT":
                return "Dữ liệu đầu vào không hợp lệ.";
            
            case "SYSTEM_ERROR":
                return "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.";
            
            default:
                return errorMessage != null ? errorMessage : "Lỗi không xác định: " + errorCode;
        }
    }
}