package com.motorbike.domain.exceptions;

public class InvalidOrderException extends BusinessException {
    public InvalidOrderException(String errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidOrderException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
