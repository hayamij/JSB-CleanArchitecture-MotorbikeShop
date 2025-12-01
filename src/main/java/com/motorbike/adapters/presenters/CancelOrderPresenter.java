package com.motorbike.adapters.presenters;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;

public class CancelOrderPresenter implements CancelOrderOutputBoundary {
    
    private final CancelOrderViewModel viewModel;
    private static final NumberFormat VND_FORMAT =
        NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    
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

    
    private void presentSuccess(CancelOrderOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        
        viewModel.orderId = outputData.getOrderId();
        viewModel.customerId = outputData.getCustomerId();
        viewModel.orderStatus = formatOrderStatus(outputData.getOrderStatus());
        viewModel.formattedRefundAmount = formatCurrency(outputData.getRefundAmount());
        viewModel.cancelReason = outputData.getCancelReason();
        viewModel.cancelledAtDisplay = formatDateTime(outputData.getCancelledAt());
        
        viewModel.message = String.format(
            "Hủy đơn hàng thành công! Mã đơn: #%d. Hoàn tiền: %s",
            outputData.getOrderId(),
            viewModel.formattedRefundAmount
        );
        
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.errorColor = null;
    }

    
    private void presentError(CancelOrderOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(
            outputData.getErrorCode(),
            outputData.getErrorMessage()
        );
        viewModel.errorColor = determineErrorColor(outputData.getErrorCode());
        
        viewModel.message = "Hủy đơn hàng thất bại";
        
        viewModel.orderId = null;
        viewModel.customerId = null;
        viewModel.orderStatus = null;
        viewModel.formattedRefundAmount = null;
        viewModel.cancelReason = null;
        viewModel.cancelledAtDisplay = null;
    }

    
    private String formatCurrency(java.math.BigDecimal amount) {
        if (amount == null) {
            return "₫0";
        }
        return VND_FORMAT.format(amount);
    }

    
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

    
    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    
    private String determineErrorColor(String errorCode) {
        if (errorCode == null) {
            return "RED";
        }
        
        switch (errorCode) {
            case "INVALID_ORDER_STATUS":
            case "CANNOT_CANCEL_ORDER":
                return "ORANGE";
            case "PERMISSION_DENIED":
            case "ORDER_NOT_FOUND":
                return "RED";
            default:
                return "RED";
        }
    }

    
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
