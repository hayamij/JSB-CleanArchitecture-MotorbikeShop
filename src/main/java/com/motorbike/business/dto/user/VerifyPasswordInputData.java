package com.motorbike.business.dto.user;

public class VerifyPasswordInputData {
    private final String plainPassword;
    private final String hashedPassword;
    
    public VerifyPasswordInputData(String plainPassword, String hashedPassword) {
        this.plainPassword = plainPassword;
        this.hashedPassword = hashedPassword;
    }
    
    public String getPlainPassword() { return plainPassword; }
    public String getHashedPassword() { return hashedPassword; }
}
