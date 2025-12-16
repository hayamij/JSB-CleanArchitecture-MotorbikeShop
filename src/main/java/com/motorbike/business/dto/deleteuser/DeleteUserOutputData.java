package com.motorbike.business.dto.deleteuser;

public class DeleteUserOutputData {
    private final boolean success;
    private final String errorCode;
    private final String message;

    private DeleteUserOutputData(boolean success, String errorCode, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static DeleteUserOutputData forSuccess() {
        return new DeleteUserOutputData(true, null, null);
    }

    public static DeleteUserOutputData forError(String errorCode, String message) {
        return new DeleteUserOutputData(false, errorCode, message);
    }

    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
}