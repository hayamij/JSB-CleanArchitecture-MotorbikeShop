package com.motorbike.domain.exceptions;

public class EmptyPasswordException extends InvalidInputException {
    public EmptyPasswordException() {
        super("Mật khẩu không được để trống", "EMPTY_PASSWORD");
    }
}
