package com.motorbike.domain.exceptions;

/**
 * Exception cho tất cả lỗi validation input
 */
public class ValidationException extends IllegalArgumentException {
    private final String errorCode;
    
    public ValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    // ===== VALIDATION METHODS - EMAIL =====
    public static ValidationException emptyEmail() {
        return new ValidationException("Email không được để trống", "EMPTY_EMAIL");
    }
    
    public static ValidationException invalidEmail() {
        return new ValidationException("Email không hợp lệ", "INVALID_EMAIL");
    }
    
    // ===== VALIDATION METHODS - PASSWORD =====
    public static ValidationException emptyPassword() {
        return new ValidationException("Mật khẩu không được để trống", "EMPTY_PASSWORD");
    }
    
    // ===== VALIDATION METHODS - IDS =====
    public static ValidationException invalidUserId() {
        return new ValidationException("User ID không hợp lệ", "INVALID_USER_ID");
    }
    
    public static ValidationException invalidProductId() {
        return new ValidationException("Mã sản phẩm không hợp lệ", "INVALID_PRODUCT_ID");
    }
    
    // ===== VALIDATION METHODS - QUANTITY =====
    public static ValidationException invalidQuantity() {
        return new ValidationException("Số lượng không hợp lệ", "INVALID_QUANTITY");
    }
    
    // ===== VALIDATION METHODS - USER =====
    public static ValidationException emptyUsername() {
        return new ValidationException("Tên đăng nhập không được rỗng", "EMPTY_USERNAME");
    }
    
    public static ValidationException usernameTooShort() {
        return new ValidationException("Tên đăng nhập phải >= 3 ký tự", "USERNAME_TOO_SHORT");
    }
    
    public static ValidationException usernameTooLong() {
        return new ValidationException("Tên đăng nhập phải <= 50 ký tự", "USERNAME_TOO_LONG");
    }
    
    public static ValidationException passwordTooShort() {
        return new ValidationException("Mật khẩu phải >= 6 ký tự", "PASSWORD_TOO_SHORT");
    }
    
    public static ValidationException emptyPhone() {
        return new ValidationException("Số điện thoại không được rỗng", "EMPTY_PHONE");
    }
    
    public static ValidationException invalidPhoneFormat() {
        return new ValidationException(
            "Số điện thoại không đúng định dạng (VD: 0912345678 hoặc +84912345678)", 
            "INVALID_PHONE_FORMAT"
        );
    }
    
    public static ValidationException accountInactive() {
        return new ValidationException("Tài khoản đã bị khóa", "ACCOUNT_INACTIVE");
    }
    
    // ===== VALIDATION METHODS - INPUT DATA =====
    public static ValidationException invalidInput() {
        return new ValidationException("Input data không hợp lệ", "INVALID_INPUT");
    }
    
    // ===== VALIDATION METHODS - PRODUCT =====
    public static ValidationException emptyProductName() {
        return new ValidationException("Tên sản phẩm không được rỗng", "EMPTY_NAME");
    }
    
    public static ValidationException productNameTooLong() {
        return new ValidationException("Tên sản phẩm phải <= 255 ký tự", "NAME_TOO_LONG");
    }
    
    public static ValidationException nullPrice() {
        return new ValidationException("Giá không được null", "NULL_PRICE");
    }
    
    public static ValidationException invalidPrice() {
        return new ValidationException("Giá phải > 0", "INVALID_PRICE");
    }
    
    public static ValidationException negativeStock() {
        return new ValidationException("Số lượng tồn kho không được âm", "NEGATIVE_STOCK");
    }
    
    public static ValidationException nullProductId() {
        return new ValidationException("Mã sản phẩm không được null", "NULL_PRODUCT_ID");
    }
    
    // ===== VALIDATION METHODS - CART =====
    public static ValidationException nullItem() {
        return new ValidationException("Chi tiết giỏ hàng không được null", "NULL_ITEM");
    }
    
    public static ValidationException nullCartId() {
        return new ValidationException("Mã giỏ hàng không được null", "NULL_CART_ID");
    }
    
    public static ValidationException invalidCartQuantity() {
        return new ValidationException("Số lượng không được âm", "INVALID_QUANTITY");
    }
    
    public static ValidationException invalidCartQuantity(String message) {
        return new ValidationException(message, "INVALID_QUANTITY");
    }
    
    public static ValidationException quantityTooLow() {
        return new ValidationException(
            "Không thể giảm số lượng xuống <= 0. Hãy xóa sản phẩm thay vì giảm số lượng.", 
            "QUANTITY_TOO_LOW"
        );
    }
    
    public static ValidationException invalidCartPrice() {
        return new ValidationException("Giá phải > 0", "INVALID_PRICE");
    }
    
    // ===== VALIDATION METHODS - ORDER =====
    public static ValidationException missingReceiverName() {
        return new ValidationException("Tên người nhận không được để trống", "MISSING_RECEIVER_NAME");
    }
    
    public static ValidationException missingPhone() {
        return new ValidationException("Số điện thoại không được để trống", "MISSING_PHONE");
    }
    
    public static ValidationException missingAddress() {
        return new ValidationException("Địa chỉ giao hàng không được để trống", "MISSING_ADDRESS");
    }
    
    public static ValidationException nullOrderId() {
        return new ValidationException("Mã đơn hàng không được null", "NULL_ORDER_ID");
    }
    
    public static ValidationException nullOrderItem() {
        return new ValidationException("Chi tiết đơn hàng không được null", "NULL_ORDER_ITEM");
    }
    
    public static ValidationException nullStatus() {
        return new ValidationException("Trạng thái mới không được null", "NULL_STATUS");
    }
    
    public static ValidationException nullCart() {
        return new ValidationException("Giỏ hàng không được null", "NULL_CART");
    }
    
    public static ValidationException nullCartItem() {
        return new ValidationException("Chi tiết giỏ hàng không được null", "NULL_CART_ITEM");
    }
    
    public static ValidationException invalidOrderQuantity(String message) {
        return new ValidationException(message, "INVALID_QUANTITY");
    }
    
    public static ValidationException invalidOrderPrice(String message) {
        return new ValidationException(message, "INVALID_PRICE");
    }
}
