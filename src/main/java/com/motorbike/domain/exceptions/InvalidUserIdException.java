package com.motorbike.domain.exceptions;

public class InvalidUserIdException extends InvalidInputException {
    public InvalidUserIdException() {
        super("User ID không hợp lệ", "INVALID_USER_ID");
    }
}
