package com.motorbike.business.dto.login;

public class LoginInputData {
    private final String email;
    private final String password;
    
    private final Long guestCartId;

    public LoginInputData(String email, String password) {
        this.email = email;
        this.password = password;
        this.guestCartId = null;
    }

    public LoginInputData(String email, String password, Long guestCartId) {
        this.email = email;
        this.password = password;
        this.guestCartId = guestCartId;
    }

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public Long getGuestCartId() {return guestCartId;}
}
