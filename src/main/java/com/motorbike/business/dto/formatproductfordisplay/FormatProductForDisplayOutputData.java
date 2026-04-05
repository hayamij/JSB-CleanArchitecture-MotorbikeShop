package com.motorbike.business.dto.formatproductfordisplay;

public class FormatProductForDisplayOutputData {
    private final boolean success;
    private final Long productId;
    private final String productName;
    private final String productCode;
    private final String formattedPrice;
    private final String formattedDiscountPrice;
    private final String stockStatus;
    private final String stockStatusColor;
    private final String discountBadge;
    private final String categoryDisplay;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public FormatProductForDisplayOutputData(
            Long productId,
            String productName,
            String productCode,
            String formattedPrice,
            String formattedDiscountPrice,
            String stockStatus,
            String stockStatusColor,
            String discountBadge,
            String categoryDisplay) {
        this.success = true;
        this.productId = productId;
        this.productName = productName;
        this.productCode = productCode;
        this.formattedPrice = formattedPrice;
        this.formattedDiscountPrice = formattedDiscountPrice;
        this.stockStatus = stockStatus;
        this.stockStatusColor = stockStatusColor;
        this.discountBadge = discountBadge;
        this.categoryDisplay = categoryDisplay;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private FormatProductForDisplayOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.productId = null;
        this.productName = null;
        this.productCode = null;
        this.formattedPrice = null;
        this.formattedDiscountPrice = null;
        this.stockStatus = null;
        this.stockStatusColor = null;
        this.discountBadge = null;
        this.categoryDisplay = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static FormatProductForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatProductForDisplayOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public String getFormattedDiscountPrice() {
        return formattedDiscountPrice;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public String getStockStatusColor() {
        return stockStatusColor;
    }

    public String getDiscountBadge() {
        return discountBadge;
    }

    public String getCategoryDisplay() {
        return categoryDisplay;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
