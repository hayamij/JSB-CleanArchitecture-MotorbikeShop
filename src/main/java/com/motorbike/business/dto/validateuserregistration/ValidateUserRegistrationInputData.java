package com.motorbike.business.dto.validateuserregistration;

public class ValidateUserRegistrationInputData {
    private final String email;
    private final String username;
    private final String password;
    private final String phoneNumber;
    private final String fullName;

    public ValidateUserRegistrationInputData(
            String email,
            String username,
            String password,
            String phoneNumber,
            String fullName) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }
}
