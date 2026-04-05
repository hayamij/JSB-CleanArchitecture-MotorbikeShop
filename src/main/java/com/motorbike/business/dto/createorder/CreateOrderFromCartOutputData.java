package com.motorbike.business.dto.createorder;

import com.motorbike.domain.entities.DonHang;
import java.math.BigDecimal;

public class CreateOrderFromCartOutputData {
    private final DonHang donHang;
    private final Long orderId;
    private final Long userId;
    private final BigDecimal totalAmount;
    private final int totalItems;
    private final String status;
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    // Success constructor
    public CreateOrderFromCartOutputData(DonHang order) {
        this.donHang = order;
        this.orderId = order.getMaDonHang();
        this.userId = order.getMaTaiKhoan();
        this.totalAmount = order.getTongTien();
        this.totalItems = order.getDanhSachSanPham().size();
        this.status = order.getTrangThai().name();
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    // Error constructor
    private CreateOrderFromCartOutputData(String errorCode, String errorMessage) {
        this.donHang = null;
        this.orderId = null;
        this.userId = null;
        this.totalAmount = null;
        this.totalItems = 0;
        this.status = null;
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static CreateOrderFromCartOutputData forError(String errorCode, String errorMessage) {
        return new CreateOrderFromCartOutputData(errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public DonHang getDonHang() {
        return donHang;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
