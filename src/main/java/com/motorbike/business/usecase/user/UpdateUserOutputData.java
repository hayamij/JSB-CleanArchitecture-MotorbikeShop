package com.motorbike.business.usecase.user;

public class UpdateUserOutputData {
    private final String message;
    private final boolean success;

    public UpdateUserOutputData(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() { return message; }
    public boolean isSuccess() { return success; }
}
