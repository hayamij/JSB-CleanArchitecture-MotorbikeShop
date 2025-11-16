package com.motorbike.domain.exceptions;

public class InvalidInputDataException extends InvalidInputException {
    public InvalidInputDataException() {
        super("Input data không hợp lệ", "INVALID_INPUT");
    }
}
