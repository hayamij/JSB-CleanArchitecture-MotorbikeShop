package com.motorbike.business.dto.calculateproductprice;

import java.math.BigDecimal;

public class CalculateProductPriceOutputData {
    private final boolean success;
    private final BigDecimal basePrice;
    private final Integer discountPercent;
    private final BigDecimal discountAmount;
    private final BigDecimal finalPrice;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public CalculateProductPriceOutputData(
            BigDecimal basePrice,
            Integer discountPercent,
            BigDecimal discountAmount,
            BigDecimal finalPrice) {
        this.success = true;
        this.basePrice = basePrice;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalPrice = finalPrice;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private CalculateProductPriceOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.basePrice = null;
        this.discountPercent = null;
        this.discountAmount = null;
        this.finalPrice = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static CalculateProductPriceOutputData forError(String errorCode, String errorMessage) {
        return new CalculateProductPriceOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
