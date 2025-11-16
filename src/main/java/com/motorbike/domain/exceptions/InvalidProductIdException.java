package com.motorbike.domain.exceptions;

public class InvalidProductIdException extends InvalidInputException {
    public InvalidProductIdException() {
        super("Mã sản phẩm không hợp lệ", "INVALID_PRODUCT_ID");
    }
}
