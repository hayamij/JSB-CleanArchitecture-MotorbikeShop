package com.motorbike.business.dto.login;

public class LoginInputData {
    private final String username;  // Can be username, email, or phone
    private final String password;
    
    private final Long guestCartId;

    public LoginInputData(String username, String password) {
        this.username = username;
        this.password = password;
        this.guestCartId = null;
    }

    public LoginInputData(String username, String password, Long guestCartId) {
        this.username = username;
        this.password = password;
        this.guestCartId = guestCartId;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public Long getGuestCartId() {return guestCartId;}
}
