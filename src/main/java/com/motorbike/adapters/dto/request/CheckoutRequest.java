package com.motorbike.adapters.dto.request;

/**
 * Request DTO for checkout
 */
public class CheckoutRequest {
    private Long userId;
    private String receiverName;
    private String phoneNumber;
    private String shippingAddress;
    private String note;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
