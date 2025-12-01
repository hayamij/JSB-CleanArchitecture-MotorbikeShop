package com.motorbike.business.dto.register;

public class RegisterInputData {
    private final String email;
    private final String username;
    private final String password;
    private final String confirmPassword;
    private final String phoneNumber;
    private final String address;

    public RegisterInputData(String email, String username, String password,
                            String confirmPassword, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
        this.address = null;
    }

    public RegisterInputData(String email, String username, String password,
                            String confirmPassword, String phoneNumber, String address) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getEmail() {return email;}

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public String getConfirmPassword() {return confirmPassword;}

    public String getPhoneNumber() {return phoneNumber;}

    public String getAddress() {return address;}
}
