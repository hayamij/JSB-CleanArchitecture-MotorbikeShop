package com.motorbike.domain.exceptions;

/**
 * InvalidOrderException
 * Exception được throw khi có lỗi xảy ra trong quá trình xử lý đơn hàng
 */
public class InvalidOrderException extends BusinessException {
    public InvalidOrderException(String errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidOrderException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
