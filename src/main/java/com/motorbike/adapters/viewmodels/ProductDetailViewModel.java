package com.motorbike.adapters.viewmodels;

/**
 * ViewModel for Product Detail
 * Contains already formatted data ready for display
 * NO logic, pure data container
 */
public class ProductDetailViewModel {
    // Display strings - already formatted
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
    
    // Error display
    public boolean hasError;
    public String errorMessage;
    public String errorColor;
    
    public ProductDetailViewModel() {
        this.errorColor = "RED";
        this.stockStatusColor = "GREEN";
    }
    
    // Simple getters for convenience
    public boolean hasError() {
        return hasError;
    }
    
    public String getFormattedPrice() {
        return formattedPrice;
    }
    
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }
}
