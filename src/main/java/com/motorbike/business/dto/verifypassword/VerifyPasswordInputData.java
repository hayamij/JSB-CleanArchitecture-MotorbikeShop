package com.motorbike.business.dto.verifypassword;

public class VerifyPasswordInputData {
    private final String plainPassword;
    private final String hashedPassword;

    public VerifyPasswordInputData(String plainPassword, String hashedPassword) {
        this.plainPassword = plainPassword;
        this.hashedPassword = hashedPassword;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
