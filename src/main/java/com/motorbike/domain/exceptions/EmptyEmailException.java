package com.motorbike.domain.exceptions;

public class EmptyEmailException extends InvalidInputException {
    public EmptyEmailException() {
        super("Email không được để trống", "EMPTY_EMAIL");
    }
}
