package com.motorbike.business.dto.deleteuser;

public class DeleteUserInputData {
    private final boolean admin;
    private final Long userId;

    private DeleteUserInputData(boolean admin, Long userId) {
        this.admin = admin;
        this.userId = userId;
    }

    public static DeleteUserInputData forAdmin(boolean admin, Long userId) {
        return new DeleteUserInputData(admin, userId);
    }

    public boolean isAdmin() {
        return admin;
    }

    public Long getUserId() {
        return userId;
    }
}