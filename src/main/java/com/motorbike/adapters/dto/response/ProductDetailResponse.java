package com.motorbike.adapters.dto.response;

/**
 * Response DTO for product detail
 */
public class ProductDetailResponse {
    private boolean success;
    private String productId;
    private String name;
    private String description;
    private String formattedPrice;
    private String imageUrl;
    private String specifications;
    private String categoryDisplay;
    private String stockQuantity;
    private String availabilityStatus;
    private String errorCode;
    private String errorMessage;

    public ProductDetailResponse(boolean success, String productId, String name, 
                                String description, String formattedPrice, String imageUrl,
                                String specifications, String categoryDisplay, 
                                String stockQuantity, String availabilityStatus,
                                String errorCode, String errorMessage) {
        this.success = success;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.formattedPrice = formattedPrice;
        this.imageUrl = imageUrl;
        this.specifications = specifications;
        this.categoryDisplay = categoryDisplay;
        this.stockQuantity = stockQuantity;
        this.availabilityStatus = availabilityStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getFormattedPrice() { return formattedPrice; }
    public String getImageUrl() { return imageUrl; }
    public String getSpecifications() { return specifications; }
    public String getCategoryDisplay() { return categoryDisplay; }
    public String getStockQuantity() { return stockQuantity; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
