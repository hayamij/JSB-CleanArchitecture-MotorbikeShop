package com.motorbike.adapters.dto.response;

public class DeleteUserResponse {
    public final boolean success;
    public final String errorCode;
    public final String message;

    private DeleteUserResponse(boolean success, String errorCode, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static DeleteUserResponse success() {
        return new DeleteUserResponse(true, null, null);
    }

    public static DeleteUserResponse error(String errorCode, String message) {
        return new DeleteUserResponse(false, errorCode, message);
    }
}