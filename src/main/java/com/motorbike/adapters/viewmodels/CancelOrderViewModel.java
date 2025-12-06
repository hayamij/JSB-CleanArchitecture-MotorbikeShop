package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class CancelOrderViewModel implements CancelOrderOutputBoundary {
    
    public boolean success;
    public String message;
    
    public Long orderId;
    public Long customerId;
    public String orderStatus;
    public String formattedRefundAmount;
    public String cancelReason;
    public String cancelledAtDisplay;
    
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor;
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public CancelOrderViewModel() {
        this.success = false;
        this.hasError = false;
    }

    @Override
    public void present(CancelOrderOutputData outputData) {
        if (outputData.isSuccess()) {
            this.success = true;
            this.hasError = false;
            this.message = "Hủy đơn hàng thành công";
            this.orderId = outputData.getOrderId();
            this.customerId = outputData.getCustomerId();
            this.orderStatus = outputData.getOrderStatus();
            this.formattedRefundAmount = formatCurrency(outputData.getRefundAmount());
            this.cancelReason = outputData.getCancelReason();
            this.cancelledAtDisplay = outputData.getCancelledAt() != null 
                ? outputData.getCancelledAt().format(DATE_FORMATTER) 
                : null;
            this.errorCode = null;
            this.errorMessage = null;
        } else {
            this.success = false;
            this.hasError = true;
            this.message = "Không thể hủy đơn hàng";
            this.errorCode = outputData.getErrorCode();
            this.errorMessage = outputData.getErrorMessage();
            this.orderId = null;
            this.customerId = null;
            this.orderStatus = null;
            this.formattedRefundAmount = null;
            this.cancelReason = null;
            this.cancelledAtDisplay = null;
        }
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0 ₫";
        return String.format("%,.0f ₫", amount);
    }
}
