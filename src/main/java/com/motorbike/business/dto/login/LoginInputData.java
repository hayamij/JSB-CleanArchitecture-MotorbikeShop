package com.motorbike.business.dto.login;

/**
 * Input DTO for Login Use Case
 * Carries data INTO the use case from the adapter layer
 * Plain data structure - no business logic
 */
public class LoginInputData {
    private final String email;
    private final String password;
    
    // Optional: session cart data for guest cart merging
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

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getGuestCartId() {
        return guestCartId;
    }
}
