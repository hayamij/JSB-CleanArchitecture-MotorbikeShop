package com.motorbike.adapters.viewmodels;

public class CheckInventoryAvailabilityViewModel {
    public boolean success;
    public boolean available;
    public int availableStock;
    public String productName;
    public Long productId;
    public String message;
    public boolean hasError;
    public String errorCode;
    public String errorMessage;

    public CheckInventoryAvailabilityViewModel() {
        this.success = false;
        this.available = false;
        this.availableStock = 0;
        this.productName = null;
        this.productId = null;
        this.message = null;
        this.hasError = false;
        this.errorCode = null;
        this.errorMessage = null;
    }
}
