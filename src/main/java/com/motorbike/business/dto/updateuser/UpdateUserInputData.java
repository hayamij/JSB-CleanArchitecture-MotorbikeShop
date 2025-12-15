package com.motorbike.business.dto.updateuser;

public class UpdateUserInputData {
    private final boolean admin;
    private final Long userId;
    public final String email;
    public final String username;
    public final String password;
    public final String phoneNumber;
    public final String address;
    public final String role;
    public final Boolean active;

    private UpdateUserInputData(boolean admin, Long userId, String email, String username,
                                String password, String phoneNumber, String address,
                                String role, Boolean active) {
        this.admin = admin;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.active = active;
    }

    public static UpdateUserInputData forAdmin(boolean admin, Long userId,
                                               String email, String username,
                                               String password, String phoneNumber,
                                               String address, String role, Boolean active) {
        return new UpdateUserInputData(admin, userId, email, username, password, phoneNumber, address, role, active);
    }

    public boolean isAdmin() { return admin; }
    public Long getUserId() { return userId; }
}