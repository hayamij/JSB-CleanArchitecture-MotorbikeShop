package com.motorbike.business.dto.checkout;

/**
 * DTO: CheckoutInputData
 * Input data for checkout use case
 * Business Rule: Bắt buộc phải có thông tin người nhận và địa chỉ giao hàng
 */
public class CheckoutInputData {
    private final Long userId;
    private final String receiverName;
    private final String phoneNumber;
    private final String shippingAddress;
    private final String note;

    public CheckoutInputData(Long userId, String receiverName, String phoneNumber, 
                            String shippingAddress, String note) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.note = note;
    }

    public Long getUserId() {
        return userId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getNote() {
        return note;
    }
}
