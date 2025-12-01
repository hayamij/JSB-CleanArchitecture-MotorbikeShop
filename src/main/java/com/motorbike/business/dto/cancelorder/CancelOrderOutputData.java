package com.motorbike.business.dto.cancelorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CancelOrderOutputData {
    private final boolean success;
    private final Long orderId;
    private final Long customerId;
    private final String orderStatus;
    private final BigDecimal refundAmount;
    private final String cancelReason;
    private final LocalDateTime cancelledAt;
    private final String errorCode;
    private final String errorMessage;

    public CancelOrderOutputData(Long orderId, Long customerId, String orderStatus,
                                 BigDecimal refundAmount, String cancelReason,
                                 LocalDateTime cancelledAt) {
        this.success = true;
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.refundAmount = refundAmount;
        this.cancelReason = cancelReason;
        this.cancelledAt = cancelledAt;
        this.errorCode = null;
        this.errorMessage = null;
    }

    public CancelOrderOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.orderId = null;
        this.customerId = null;
        this.orderStatus = null;
        this.refundAmount = null;
        this.cancelReason = null;
        this.cancelledAt = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getOrderId() {return orderId;}

    public Long getCustomerId() {return customerId;}

    public String getOrderStatus() {return orderStatus;}

    public BigDecimal getRefundAmount() {return refundAmount;}

    public String getCancelReason() {return cancelReason;}

    public LocalDateTime getCancelledAt() {return cancelledAt;}

    public String getErrorCode() {return errorCode;}

    public String getErrorMessage() {return errorMessage;}

    public static CancelOrderOutputData forSuccess(Long orderId, Long customerId,
                                                   String orderStatus, BigDecimal refundAmount,
                                                   String cancelReason) {
        return new CancelOrderOutputData(orderId, customerId, orderStatus,
                                        refundAmount, cancelReason, LocalDateTime.now());
    }

    public static CancelOrderOutputData forError(String errorCode, String errorMessage) {
        return new CancelOrderOutputData(errorCode, errorMessage);
    }
}
