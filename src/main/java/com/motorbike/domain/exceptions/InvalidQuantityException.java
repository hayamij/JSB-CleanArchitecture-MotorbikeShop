package com.motorbike.domain.exceptions;

public class InvalidQuantityException extends InvalidInputException {
    public InvalidQuantityException() {
        super("Số lượng không hợp lệ", "INVALID_QUANTITY");
    }
}
