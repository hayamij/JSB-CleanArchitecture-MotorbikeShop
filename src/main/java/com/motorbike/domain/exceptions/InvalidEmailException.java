package com.motorbike.domain.exceptions;

public class InvalidEmailException extends InvalidInputException {
    public InvalidEmailException() {
        super("Email không hợp lệ", "INVALID_EMAIL");
    }
}
