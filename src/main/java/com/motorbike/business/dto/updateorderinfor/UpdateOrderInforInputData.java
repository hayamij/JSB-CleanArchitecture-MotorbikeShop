package com.motorbike.business.dto.updateorderinfor;

public class UpdateOrderInforInputData {
    private final Long orderId;
    private final Long userId;
    private final String receiverName;
    private final String phoneNumber;
    private final String shippingAddress;
    private final String note;

    public UpdateOrderInforInputData(Long orderId,
                                     Long userId,
                                     String receiverName,
                                     String phoneNumber,
                                     String shippingAddress,
                                     String note) {
        this.orderId = orderId;
        this.userId = userId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.note = note;
    }

    public Long getOrderId() {return orderId;}
    public Long getUserId() {return userId;}
    public String getReceiverName() {return receiverName;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getShippingAddress() {return shippingAddress;}
    public String getNote() {return note;}
}
