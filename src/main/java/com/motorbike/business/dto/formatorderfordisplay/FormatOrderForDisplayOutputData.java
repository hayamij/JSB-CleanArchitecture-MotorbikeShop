package com.motorbike.business.dto.formatorderfordisplay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FormatOrderForDisplayOutputData {
    private final Long orderId;
    private final String statusText;
    private final String statusColor;
    private final String formattedAmount;
    private final String formattedDate;
    private final String paymentMethodText;
    private final boolean canCancel;
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    public FormatOrderForDisplayOutputData(
            Long orderId,
            String statusText,
            String statusColor,
            String formattedAmount,
            String formattedDate,
            String paymentMethodText,
            boolean canCancel) {
        this.orderId = orderId;
        this.statusText = statusText;
        this.statusColor = statusColor;
        this.formattedAmount = formattedAmount;
        this.formattedDate = formattedDate;
        this.paymentMethodText = paymentMethodText;
        this.canCancel = canCancel;
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    private FormatOrderForDisplayOutputData(String errorCode, String errorMessage) {
        this.orderId = null;
        this.statusText = null;
        this.statusColor = null;
        this.formattedAmount = null;
        this.formattedDate = null;
        this.paymentMethodText = null;
        this.canCancel = false;
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static FormatOrderForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatOrderForDisplayOutputData(errorCode, errorMessage);
    }
    
    public boolean isSuccess() { return success; }
    public Long getOrderId() { return orderId; }
    public String getStatusText() { return statusText; }
    public String getStatusColor() { return statusColor; }
    public String getFormattedAmount() { return formattedAmount; }
    public String getFormattedDate() { return formattedDate; }
    public String getPaymentMethodText() { return paymentMethodText; }
    public boolean canCancel() { return canCancel; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    
    // Alias methods for test compatibility
    public String getFormattedStatus() { return statusText; }
    public String getTotalAmount() { return formattedAmount; }
    public String getFormattedOrderDate() { return formattedDate; }
    public String getFormattedPaymentMethod() { return paymentMethodText; }
}
