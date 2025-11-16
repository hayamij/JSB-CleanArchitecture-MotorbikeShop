package com.motorbike.domain.exceptions;

public class PasswordMismatchException extends InvalidInputException {
    public PasswordMismatchException() {
        super("Mật khẩu xác nhận không khớp", "PASSWORD_MISMATCH");
    }
}
