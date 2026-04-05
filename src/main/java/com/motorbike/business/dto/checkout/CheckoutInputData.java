package com.motorbike.business.dto.checkout;

public class CheckoutInputData {
    private final Long userId;
    private final String receiverName;
    private final String phoneNumber;
    private final String shippingAddress;
    private final String note;
    private final String paymentMethod;

    public CheckoutInputData(Long userId, String receiverName, String phoneNumber,
                            String shippingAddress, String note, String paymentMethod) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.note = note;
        this.paymentMethod = paymentMethod;
    }

    public Long getUserId() {return userId;}

    public String getReceiverName() {return receiverName;}

    public String getPhoneNumber() {return phoneNumber;}

    public String getShippingAddress() {return shippingAddress;}

    public String getNote() {return note;}
    
    public String getPaymentMethod() {return paymentMethod;}
}
