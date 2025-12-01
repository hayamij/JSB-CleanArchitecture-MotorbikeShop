package com.motorbike.adapters.viewmodels;

public class ProductDetailViewModel {
    public String productId;
    public String name;
    public String description;
    public String formattedPrice;
    public String imageUrl;
    public String specifications;
    public String categoryDisplay;
    public String stockQuantity;
    public String availabilityStatus;
    public String stockStatusColor;
    public String errorCode;
    
    public boolean hasError;
    public String errorMessage;
    public String errorColor;
    
    public ProductDetailViewModel() {
        this.errorColor = "RED";
        this.stockStatusColor = "GREEN";
    }
    
    public boolean hasError() {
        return hasError;
    }
    
    public String getErrorCode() {return errorCode;}
    
    public String getFormattedPrice() {return formattedPrice;}
    
    public String getAvailabilityStatus() {return availabilityStatus;}
}
