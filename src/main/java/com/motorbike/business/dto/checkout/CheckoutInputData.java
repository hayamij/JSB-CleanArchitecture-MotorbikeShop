package com.motorbike.business.dto.checkout;

/**
 * DTO: CheckoutInputData
 * Input data for checkout use case
 */
public class CheckoutInputData {
    private final Long userId;
    private final String shippingAddress;
    private final String customerPhone;

    public CheckoutInputData(Long userId, String shippingAddress, String customerPhone) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.customerPhone = customerPhone;
    }

    public Long getUserId() {
        return userId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
}
