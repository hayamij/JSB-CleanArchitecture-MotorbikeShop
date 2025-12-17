package com.motorbike.domain.exceptions;

/**
 * Exception cho tất cả lỗi business/domain logic
 * (not found, authentication, cart, order, stock...)
 */
public class DomainException extends RuntimeException {
    private final String errorCode;

    public DomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
    
    // ===== ENTITY NOT FOUND =====
    public static DomainException userNotFound(String identifier) {
        return new DomainException("Không tìm thấy tài khoản với thông tin: " + identifier, "USER_NOT_FOUND");
    }
    
    public static DomainException productNotFound(String productId) {
        return new DomainException("Không tìm thấy sản phẩm với mã: " + productId, "PRODUCT_NOT_FOUND");
    }
    
    public static DomainException cartNotFound() {
        return new DomainException("Không tìm thấy giỏ hàng", "CART_NOT_FOUND");
    }
    
    // ===== AUTHENTICATION =====
    public static DomainException wrongPassword() {
        return new DomainException("Mật khẩu không đúng", "WRONG_PASSWORD");
    }
    
    public static DomainException accountLocked() {
        return new DomainException("Tài khoản đã bị khóa. Vui lòng liên hệ admin.", "ACCOUNT_LOCKED");
    }
    
    public static DomainException emailAlreadyExists(String email) {
        return new DomainException("Email đã được sử dụng: " + email, "EMAIL_EXISTS");
    }
    
    public static DomainException usernameAlreadyExists(String username) {
        return new DomainException("Tên đăng nhập đã được sử dụng: " + username, "USERNAME_EXISTS");
    }
    
    // ===== CART & ORDER =====
    public static DomainException emptyCart() {
        return new DomainException("Giỏ hàng trống", "EMPTY_CART");
    }
    
    public static DomainException productNotInCart() {
        return new DomainException("Sản phẩm không có trong giỏ hàng", "PRODUCT_NOT_IN_CART");
    }
    
    public static DomainException insufficientStock(String productName, int available) {
        return new DomainException(
            "Sản phẩm '" + productName + "' không đủ hàng. Còn lại: " + available, 
            "INSUFFICIENT_STOCK"
        );
    }
    
    public static DomainException cannotCancelOrder(String reason) {
        return new DomainException("Không thể hủy đơn hàng: " + reason, "CANNOT_CANCEL_ORDER");
    }
    
    // ===== ORDER STATE =====
    public static DomainException invalidOrderState() {
        return new DomainException(
            "Không thể thêm sản phẩm vào đơn hàng đã được xác nhận", 
            "INVALID_ORDER_STATE"
        );
    }
    
    public static DomainException emptyOrder() {
        return new DomainException("Đơn hàng phải có ít nhất 1 sản phẩm", "EMPTY_ORDER");
    }
    
    public static DomainException invalidTotal() {
        return new DomainException("Tổng tiền đơn hàng phải lớn hơn 0", "INVALID_TOTAL");
    }
    
    public static DomainException invalidStatusTransition(String fromStatus, String toStatus) {
        return new DomainException(
            String.format("Không thể chuyển từ trạng thái '%s' sang '%s'", fromStatus, toStatus),
            "INVALID_STATUS_TRANSITION"
        );
    }
    
    public static DomainException cannotCancelDelivered() {
        return new DomainException("Không thể hủy đơn hàng đã giao", "CANNOT_CANCEL_DELIVERED");
    }
    
    public static DomainException alreadyCancelled() {
        return new DomainException("Đơn hàng đã bị hủy trước đó", "ALREADY_CANCELLED");
    }
    
    // ===== PRODUCT =====
    public static DomainException noStockToRestore() {
        return new DomainException(
            "Không thể khôi phục kinh doanh khi không có hàng trong kho", 
            "NO_STOCK_TO_RESTORE"
        );
    }
    
    public static DomainException productNotFound(Long productId) {
        return new DomainException("Không tìm thấy sản phẩm với mã: " + productId, "PRODUCT_NOT_FOUND");
    }
    
    public static DomainException productNotMotorbike() {
        return new DomainException("Sản phẩm không phải là xe máy", "PRODUCT_NOT_MOTORBIKE");
    }
    
    public static DomainException productNotAccessory() {
        return new DomainException("Sản phẩm không phải là phụ kiện", "PRODUCT_NOT_ACCESSORY");
    }
    
    // ===== ORDER =====
    public static DomainException orderNotFound(Long orderId) {
        return new DomainException("Không tìm thấy đơn hàng với mã: " + orderId, "ORDER_NOT_FOUND");
    }
    
    public static DomainException userNotFound(Long userId) {
        return new DomainException("Không tìm thấy tài khoản với mã: " + userId, "USER_NOT_FOUND");
    }
    
    public static DomainException productAlreadyExists(String productName) {
        return new DomainException("Sản phẩm đã tồn tại: " + productName, "PRODUCT_EXISTS");
    }
    
    // Overload methods for compatibility
    public static DomainException userNotFound() {
        return new DomainException("Không tìm thấy tài khoản", "USER_NOT_FOUND");
    }
    
    public static DomainException notFound(String entity, String id) {
        return new DomainException(entity + " không tìm thấy: " + id, "NOT_FOUND");
    }
}
