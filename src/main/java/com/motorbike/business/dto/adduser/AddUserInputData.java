package com.motorbike.business.dto.adduser;

public class AddUserInputData {
    public final String email;
    public final String username;
    public final String password;
    public final String phoneNumber;
    public final String address;
    public final String role; // optional, e.g. "ADMIN" or "USER"
    public final Boolean active; // optional

    private AddUserInputData(String email, String username, String password,
                             String phoneNumber, String address, String role, Boolean active) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.active = active;
    }

    public static AddUserInputData of(String email, String username, String password,
                                      String phoneNumber, String address, String role, Boolean active) {
        return new AddUserInputData(email, username, password, phoneNumber, address, role, active);
    }
}